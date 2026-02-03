package com.useai.feature.chat

import android.content.ClipData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.util.fastMap
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ChattingRepository
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.data.repository.QuestionRepository
import com.useai.core.model.chat.ChattingContent
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.ChattingStreaming
import com.useai.core.model.chat.Question
import com.useai.core.ui.runOn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatPresenter @AssistedInject constructor(
    private val chattingRepository: ChattingRepository,
    private val questionRepository: QuestionRepository,
    private val experienceRepository: ExperienceRepository,
    @Assisted private val screen: ChatScreen
) : Presenter<ChatScreen.State> {

    private val streamingChatStringBuilder = StringBuilder()

    @Composable
    override fun present(): ChatScreen.State {
        val scope = rememberStableCoroutineScope()
        val localClipboard = LocalClipboard.current

        var state by rememberRetained { mutableStateOf<ChatScreen.State>(ChatScreen.State.Loading) }
        var currentQuestion by rememberRetained { mutableStateOf(Question.EMPTY) }
        var selectedCategory by rememberRetained { mutableStateOf(ChatScreenCategory.CHATTING) }
        var userMessageInput by rememberRetained { mutableStateOf("") }

        val chattingHistories = rememberRetained {
            mutableStateMapOf<Question, ChattingHistory>()
        }
        LaunchedEffect(Unit) {
            questionRepository.getQuestions(screen.projectId).onSuccess { questions ->
                currentQuestion = questions.first()
                questions.fastMap { question ->
                    async {
                        chattingRepository.getChatHistory(question.id).onSuccess {
                            chattingHistories[question] = it
                        }.onFailure {
                            state = ChatScreen.State.LoadFailed
                        }
                    }
                }.awaitAll()

                if (state !is ChatScreen.State.LoadFailed) {
                    state = ChatScreen.State.Success(
                        questions = questions,
                        currentQuestion = currentQuestion,
                        chattingHistory = chattingHistories[currentQuestion]!!,
                        streamingStatus = ChattingStreamingStatus.Idle,
                        userInput = "",
                        currentCategory = ChatScreenCategory.CHATTING,
                        letter = currentQuestion.letter,
                    ) { event ->
                        when(event) {
                            is ChatScreen.Event.AddQuestion -> TODO()
                            is ChatScreen.Event.ChangeCategory -> {
                                selectedCategory = event.category
                            }
                            is ChatScreen.Event.ChangeQuestion -> {
                                currentQuestion = event.question
                            }
                            is ChatScreen.Event.CopyMessage -> {
                                scope.launch {
                                    localClipboard.copy(event.message)
                                }
                            }
                            is ChatScreen.Event.SendMessage -> {
                                scope.launch {
                                    chattingRepository.startChattingStream(
                                        questionId = currentQuestion.id,
                                        sendingMessage = event.message
                                    ).onStart {
                                        userMessageInput = ""
                                        chattingHistories[currentQuestion] = chattingHistories[currentQuestion]!!.copy(
                                            chattings = chattingHistories[currentQuestion]!!.chattings + ChattingContent.User(
                                                id = "",
                                                message = event.message,
                                                createdAt = LocalDateTime.now()
                                            )
                                        )
                                    }.catch {
                                        state.runOn<ChatScreen.State.Success> {
                                            state = this.copy(streamingStatus = ChattingStreamingStatus.Error)
                                        }
                                    }.collect { streaming ->
                                        when (streaming) {
                                            is ChattingStreaming.Streaming -> {
                                                state.runOn<ChatScreen.State.Success> {
                                                    state = this.copy(
                                                        streamingStatus =
                                                            ChattingStreamingStatus.Streaming(
                                                                streamingChatStringBuilder.append(
                                                                    streaming.data
                                                                ).toString()
                                                            )
                                                    )
                                                }
                                            }

                                            is ChattingStreaming.Done -> {
                                                streamingChatStringBuilder.clear()
                                                state.runOn<ChatScreen.State.Success> {
                                                    state = this.copy(
                                                        streamingStatus = ChattingStreamingStatus.Idle,
                                                        chattingHistory = chattingHistory.copy(
                                                            chattings = buildList {
                                                                addAll(chattingHistory.chattings)
                                                                add(ChattingContent.AI(
                                                                    id = streaming.chatId,
                                                                    message = streamingChatStringBuilder.toString(),
                                                                    createdAt = LocalDateTime.now(),
                                                                    isLetter = streaming.isDraft
                                                                ))
                                                            }
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            is ChatScreen.Event.UpdateLetter -> {
                                scope.launch {
                                    questionRepository.updateQuestion(
                                        projectId = screen.projectId,
                                        question = currentQuestion.copy(letter = event.letter)
                                    ).onSuccess { question ->
                                        if (currentQuestion.id == question.id) {
                                            currentQuestion = question

                                        }
                                    }
                                }
                            }
                            is ChatScreen.Event.InputMessage -> {
                                userMessageInput = event.message
                            }
                            is ChatScreen.Event.UploadExperience -> {
                                // MVP X
                            }
                        }
                    }
                }
            }.onFailure {
                state = ChatScreen.State.LoadFailed
            }
        }

        state.runOn<ChatScreen.State.Success> {
            state = this.copy(
                currentQuestion = currentQuestion,
                chattingHistory = chattingHistories[currentQuestion]!!,
                userInput = userMessageInput,
                currentCategory = selectedCategory,
                letter = currentQuestion.letter
            )
        }

        return state
    }

    private suspend fun Clipboard.copy(text: String) {
        val clipData = ClipData.newPlainText("자기소개서", text)
        val clipEntry = clipData.toClipEntry()

        setClipEntry(clipEntry)
    }

    @CircuitInject(ChatScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    interface Factory {
        fun create(screen: ChatScreen): ChatPresenter
    }
}

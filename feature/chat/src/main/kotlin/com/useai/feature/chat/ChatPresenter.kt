package com.useai.feature.chat

import android.content.ClipData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.util.fastMap
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
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

        val questions = rememberRetained { mutableStateListOf<Question>() }
        var currentQuestion by rememberRetained { mutableStateOf(Question.EMPTY) }
        var selectedCategory by rememberRetained { mutableStateOf(ChatScreenCategory.CHATTING) }
        var userMessageInput by rememberRetained { mutableStateOf("") }
        var isHeaderUIExpanded by rememberRetained { mutableStateOf(false) }
        var streamingStatus by rememberRetained { mutableStateOf<ChattingStreamingStatus>(ChattingStreamingStatus.Idle) }
        val chattingHistories = rememberRetained { mutableStateMapOf<Question, ChattingHistory>() }

        val state by produceRetainedState<ChatScreen.State>(ChatScreen.State.Loading) {
            questionRepository.getQuestions(screen.projectId).onSuccess { initialQuestions ->
                questions.addAll(initialQuestions)
                currentQuestion = initialQuestions.first()
                initialQuestions.fastMap { question ->
                    async {
                        chattingRepository.getChatHistory(question.id).onSuccess {
                            chattingHistories[question] = it
                        }.onFailure {
                            value = ChatScreen.State.LoadFailed
                        }
                    }
                }.awaitAll()

                if (value !is ChatScreen.State.LoadFailed) {
                    value = ChatScreen.State.Success(
                        questions = initialQuestions,
                        currentQuestion = currentQuestion,
                        chattingHistory = chattingHistories[currentQuestion]!!,
                        streamingStatus = ChattingStreamingStatus.Idle,
                        userInput = "",
                        currentCategory = ChatScreenCategory.CHATTING,
                        isHeaderUIExpanded = isHeaderUIExpanded
                    ) { event ->
                        when(event) {
                            is ChatScreen.Event.AddQuestion -> {

                            }
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
                                                id = LocalDateTime.now().toString(),
                                                message = event.message,
                                                createdAt = LocalDateTime.now()
                                            )
                                        )
                                    }.catch {
                                        streamingStatus = ChattingStreamingStatus.Error
                                    }.collect { streaming ->
                                        when (streaming) {
                                            is ChattingStreaming.Streaming -> {
                                                streamingStatus = ChattingStreamingStatus.Streaming(
                                                    streamingChatStringBuilder.append(streaming.data).toString()
                                                )
                                            }

                                            is ChattingStreaming.Done -> {
                                                streamingStatus = ChattingStreamingStatus.Idle
                                                chattingHistories[currentQuestion] = chattingHistories[currentQuestion]!!.copy(
                                                    chattings = buildList {
                                                        addAll(chattingHistories[currentQuestion]!!.chattings)
                                                        add(ChattingContent.AI(
                                                            id = streaming.chatId,
                                                            message = streamingChatStringBuilder.toString(),
                                                            createdAt = LocalDateTime.now(),
                                                            isLetter = streaming.isDraft
                                                        ))
                                                    }
                                                )
                                                streamingChatStringBuilder.clear()
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
                                            chattingHistories[question] = chattingHistories[currentQuestion]!!
                                            chattingHistories.remove(currentQuestion)
                                            currentQuestion = question
                                        }
                                        initialQuestions.indexOfFirst { it.id == question.id }.let { index ->
                                            questions[index] = question
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
                            is ChatScreen.Event.ExpandOrShrinkHeader -> {
                                isHeaderUIExpanded = !isHeaderUIExpanded
                            }
                        }
                    }
                }
            }.onFailure {
                value = ChatScreen.State.LoadFailed
            }
        }

        state.runOn<ChatScreen.State.Success> {
            return this.copy(
                questions = questions,
                currentQuestion = currentQuestion,
                chattingHistory = chattingHistories[currentQuestion]!!,
                userInput = userMessageInput,
                currentCategory = selectedCategory,
                streamingStatus = streamingStatus,
                isHeaderUIExpanded = isHeaderUIExpanded
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

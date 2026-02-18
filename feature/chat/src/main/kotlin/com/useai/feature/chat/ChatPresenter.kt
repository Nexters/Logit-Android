package com.useai.feature.chat

import android.content.ClipData
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.util.fastMap
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ChattingRepository
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.data.repository.QuestionRepository
import com.useai.core.model.chat.ChattingContent
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.ChattingStreaming
import com.useai.core.model.chat.Question
import com.useai.core.model.experience.MatchingExperience
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.ui.reduce
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
    private val projectRepository: ProjectRepository,
    @Assisted private val screen: ChatScreen,
    @Assisted private val navigator: Navigator
) : Presenter<ChatScreen.State> {

    private val streamingChatStringBuilder = StringBuilder()
    private val chattingHistories = hashMapOf<Question, ChattingHistory>()
    private val matchingExperiencesList = hashMapOf<Question, List<MatchingExperience>>()

    @Composable
    override fun present(): ChatScreen.State {
        val scope = rememberStableCoroutineScope()
        val screenProvider = LocalScreenProvider.current
        val localClipboard = LocalClipboard.current

        val state by produceRetainedState<ChatScreen.State>(ChatScreen.State.Loading) {
            val projectDetailDeferred = async {
                projectRepository.getProject(screen.projectId).getOrNull()
            }

            questionRepository.getQuestions(screen.projectId).onSuccess { initialQuestions ->
                if (initialQuestions.isEmpty()) {
                    reduce { ChatScreen.State.LoadFailed }
                    return@produceRetainedState
                }

                initialQuestions.fastMap { question ->
                    async {
                        experienceRepository.getMatchingExperiences(question.id).onSuccess { matchingExperiences ->
                            this@ChatPresenter.matchingExperiencesList[question] = matchingExperiences
                        }.onFailure {
                            reduce { ChatScreen.State.LoadFailed }
                        }
                    }
                }.awaitAll()

                initialQuestions.fastMap { question ->
                    async {
                        chattingRepository.getChatHistory(question.id).onSuccess { chattingHistory ->
                            chattingHistories[question] = chattingHistory
                        }.onFailure {
                            reduce { ChatScreen.State.LoadFailed }
                        }
                    }
                }.awaitAll()

                val projectDetail = projectDetailDeferred.await()
                if (projectDetail == null)
                    reduce {
                        ChatScreen.State.LoadFailed
                    }

                if (value !is ChatScreen.State.LoadFailed) {
                    val initialQuestion = initialQuestions.first()

                    reduce {
                        ChatScreen.State.Success(
                            project = requireNotNull(projectDetail),
                            questions = initialQuestions,
                            currentQuestion = initialQuestion,
                            chattingHistory = requireNotNull(chattingHistories[initialQuestion]),
                            matchingExperiences = requireNotNull(matchingExperiencesList[initialQuestion]),
                            streamingStatus = ChattingStreamingStatus.Idle,
                            userInput = "",
                            currentCategory = ChatScreenCategory.CHATTING,
                            isHeaderUIExpanded = false
                        ) { event ->
                            when(event) {
                                is ChatScreen.Event.AddQuestion -> {
                                    navigator.goTo(screenProvider.newQuestionScreen(screen.projectId))
                                }
                                is ChatScreen.Event.ChangeCategory -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(currentCategory = event.category)
                                        }
                                    }
                                }
                                is ChatScreen.Event.ChangeQuestion -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(
                                                currentQuestion = event.question,
                                                chattingHistory = requireNotNull(chattingHistories[event.question]),
                                                matchingExperiences = requireNotNull(matchingExperiencesList[event.question])
                                            )
                                        }
                                    }
                                }
                                is ChatScreen.Event.CopyMessage -> {
                                    scope.launch {
                                        localClipboard.copy(event.message)
                                    }
                                }
                                is ChatScreen.Event.SendMessage -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        scope.launch {
                                            chattingRepository.startChattingStream(
                                                questionId = currentQuestion.id,
                                                sendingMessage = event.message,
                                                experienceIds = chattingHistory.experienceIds.toList()
                                            ).onStart {
                                                value.runOn<ChatScreen.State.Success> {
                                                    reduce {
                                                        copy(
                                                            userInput = "",
                                                            chattingHistory = chattingHistory.copy(
                                                                chattings = chattingHistory.chattings + ChattingContent.User(
                                                                    id = LocalDateTime.now().toString(),
                                                                    message = event.message,
                                                                    createdAt = LocalDateTime.now()
                                                                )
                                                            )
                                                        )
                                                    }
                                                }
                                            }.catch {
                                                value.runOn<ChatScreen.State.Success> {
                                                    reduce {
                                                        copy(streamingStatus = ChattingStreamingStatus.Error)
                                                    }
                                                }
                                            }.collect { streaming ->
                                                when (streaming) {
                                                    is ChattingStreaming.Streaming -> {
                                                        value.runOn<ChatScreen.State.Success> {
                                                            reduce {
                                                                copy(
                                                                    streamingStatus = ChattingStreamingStatus.Streaming(
                                                                        streamingChatStringBuilder.append(streaming.data).toString()
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }

                                                    is ChattingStreaming.Done -> {
                                                        value.runOn<ChatScreen.State.Success> {
                                                            reduce {
                                                                copy(
                                                                    streamingStatus = ChattingStreamingStatus.Idle,
                                                                    chattingHistory = chattingHistory.copy(
                                                                        chattings = chattingHistory.chattings + ChattingContent.AI(
                                                                            id = streaming.chatId,
                                                                            message = streamingChatStringBuilder.toString(),
                                                                            createdAt = LocalDateTime.now(),
                                                                            isLetter = streaming.isDraft
                                                                        )
                                                                    )
                                                                )
                                                            }
                                                        }
                                                        streamingChatStringBuilder.clear()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                is ChatScreen.Event.UpdateLetter -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        scope.launch {
                                            questionRepository.updateQuestion(
                                                projectId = screen.projectId,
                                                question = currentQuestion.copy(letter = event.letter)
                                            ).onSuccess { question ->
                                                this@ChatPresenter.chattingHistories[question] = chattingHistory
                                                this@ChatPresenter.chattingHistories.remove(currentQuestion)
                                                this@ChatPresenter.matchingExperiencesList[question] = matchingExperiences
                                                this@ChatPresenter.matchingExperiencesList.remove(currentQuestion)
                                                reduce {
                                                    copy(
                                                        currentQuestion = question,
                                                        questions = questions.map {
                                                            if (it.id == question.id) question else it
                                                        },
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                is ChatScreen.Event.InputMessage -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(userInput = event.message)
                                        }
                                    }
                                }
                                is ChatScreen.Event.CompleteSelectExperience -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        val currentChatHistory = chattingHistories[currentQuestion]
                                        val updatedChatHistory = currentChatHistory?.copy(experienceIds = event.experienceIds.toSet()) ?: chattingHistory.copy(experienceIds = event.experienceIds.toSet())
                                        chattingHistories[currentQuestion] = updatedChatHistory
                                        reduce {
                                            copy(
                                                showExperienceModal = false,
                                                chattingHistory = updatedChatHistory
                                            )
                                        }
                                    }
                                }
                                is ChatScreen.Event.TryUploadExperience -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showExperienceModal = true)
                                        }
                                    }
                                }
                                is ChatScreen.Event.DismissExperienceModal -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showExperienceModal = false)
                                        }
                                    }
                                }
                                is ChatScreen.Event.ExpandOrShrinkHeader -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(isHeaderUIExpanded = !isHeaderUIExpanded)
                                        }
                                    }
                                }
                                is ChatScreen.Event.NavigateBack -> {
                                    navigator.pop()
                                }
                            }
                        }
                    }
                }
            }.onFailure {
                reduce { ChatScreen.State.LoadFailed }
            }
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
        fun create(screen: ChatScreen, navigator: Navigator): ChatPresenter
    }
}

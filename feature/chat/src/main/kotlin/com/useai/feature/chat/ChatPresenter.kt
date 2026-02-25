package com.useai.feature.chat

import android.content.ClipData
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.util.fastMap
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        var editedQuestionsResult by rememberRetained {
            mutableStateOf<EditQuestionsScreen.QuestionsEditedResult?>(null)
        }
        var createdQuestionResult by rememberRetained {
            mutableStateOf<NewQuestionScreen.QuestionCreatedResult?>(null)
        }
        var pendingCreatedQuestionId by rememberRetained {
            mutableStateOf<String?>(null)
        }
        val editQuestionsNavigator = rememberAnsweringNavigator<EditQuestionsScreen.QuestionsEditedResult>(
            fallbackNavigator = navigator,
        ) {
            editedQuestionsResult = it
        }
        val newQuestionNavigator = rememberAnsweringNavigator<NewQuestionScreen.QuestionCreatedResult>(
            fallbackNavigator = navigator,
        ) {
            createdQuestionResult = it
        }

        val loadingOrFailedEventSink: (ChatScreen.Event) -> Unit = { event ->
            when (event) {
                ChatScreen.Event.NavigateBack -> navigator.pop()
                else -> Unit
            }
        }

        val state by produceRetainedState<ChatScreen.State>(
            ChatScreen.State.Loading(eventSink = loadingOrFailedEventSink)
        ) {
            val projectDetailDeferred = async {
                projectRepository.getProject(screen.projectId).onFailure {
                    Log.e(TAG, "getProject failed: $it")
                }.getOrNull()
            }

            questionRepository.getQuestions(screen.projectId).onSuccess { initialQuestions ->
                if (initialQuestions.isEmpty()) {
                    Log.e(TAG, "initialQuestions is empty")
                    reduce { ChatScreen.State.LoadFailed(eventSink = loadingOrFailedEventSink) }
                    return@produceRetainedState
                }

                val matchingExperiencesDeferred = initialQuestions.fastMap { question ->
                    async {
                        experienceRepository.getMatchingExperiences(question.id).onSuccess { matchingExperiences ->
                            this@ChatPresenter.matchingExperiencesList[question] = matchingExperiences
                        }.onFailure {
                            Log.e(TAG, "getMatchingExperiences failed: $it")
                            reduce { ChatScreen.State.LoadFailed(eventSink = loadingOrFailedEventSink) }
                        }
                    }
                }

                val chattingHistoriesDeferred = initialQuestions.fastMap { question ->
                    async {
                        chattingRepository.getChatHistory(question.id).onSuccess { chattingHistory ->
                            chattingHistories[question] = chattingHistory
                        }.onFailure {
                            Log.e(TAG, "getChatHistory failed: $it")
                            reduce { ChatScreen.State.LoadFailed(eventSink = loadingOrFailedEventSink) }
                        }
                    }
                }

                matchingExperiencesDeferred.awaitAll()
                chattingHistoriesDeferred.awaitAll()

                val projectDetail = projectDetailDeferred.await()
                if (projectDetail == null) {
                    Log.d(TAG, "projectDetail is null")
                    reduce {
                        ChatScreen.State.LoadFailed(eventSink = loadingOrFailedEventSink)
                    }
                }

                if (value !is ChatScreen.State.LoadFailed) {
                    val currentState = value as? ChatScreen.State.Success
                    val initialQuestionId = pendingCreatedQuestionId ?: currentState?.currentQuestion?.id
                    val initialQuestion = initialQuestions.firstOrNull { it.id == initialQuestionId }
                        ?: initialQuestions.first()

                    reduce {
                        ChatScreen.State.Success(
                            project = requireNotNull(projectDetail),
                            questions = initialQuestions,
                            currentQuestion = initialQuestion,
                            chattingHistory = requireNotNull(chattingHistories[initialQuestion]),
                            matchingExperiences = requireNotNull(matchingExperiencesList[initialQuestion]),
                            streamingStatus = ChattingStreamingStatus.Idle,
                            userInput = "",
                            currentCategory = currentState?.currentCategory ?: ChatScreenCategory.CHATTING,
                            isHeaderUIExpanded = false
                        ) { event ->
                            when(event) {
                                is ChatScreen.Event.ApplyEditedQuestions -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        val oldHistoriesById = chattingHistories.entries.associate { (question, history) ->
                                            question.id to history
                                        }
                                        val oldMatchingById = matchingExperiencesList.entries.associate { (question, matching) ->
                                            question.id to matching
                                        }

                                        val updatedQuestions = event.questions.map {
                                            Question(
                                                id = it.id,
                                                title = it.title,
                                                maxLength = it.maxLength,
                                                letter = it.letter
                                            )
                                        }

                                        val remappedHistories = hashMapOf<Question, ChattingHistory>()
                                        val remappedMatching = hashMapOf<Question, List<MatchingExperience>>()
                                        updatedQuestions.forEach { question ->
                                            oldHistoriesById[question.id]?.let { history ->
                                                remappedHistories[question] = history.copy(questionTitle = question.title)
                                            }
                                            oldMatchingById[question.id]?.let { matching ->
                                                remappedMatching[question] = matching
                                            }
                                        }
                                        chattingHistories.clear()
                                        chattingHistories.putAll(remappedHistories)
                                        matchingExperiencesList.clear()
                                        matchingExperiencesList.putAll(remappedMatching)

                                        val fallbackQuestion = updatedQuestions.firstOrNull() ?: return@runOn
                                        val selectedQuestion = updatedQuestions.firstOrNull {
                                            it.id == currentQuestion.id
                                        } ?: fallbackQuestion
                                        val selectedHistory = remappedHistories[selectedQuestion] ?: chattingHistory
                                        val selectedMatching = remappedMatching[selectedQuestion] ?: matchingExperiences

                                        reduce {
                                            copy(
                                                questions = updatedQuestions,
                                                currentQuestion = selectedQuestion,
                                                chattingHistory = selectedHistory,
                                                matchingExperiences = selectedMatching
                                            )
                                        }
                                    }
                                }
                                is ChatScreen.Event.AddCreatedQuestion -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        if (questions.none { it.id == event.question.id }) {
                                            val createdQuestionHistory = chattingHistory.copy(
                                                chattings = emptyList(),
                                                experienceIds = emptySet(),
                                                questionId = event.question.id,
                                                questionTitle = event.question.title,
                                                nextCursor = null,
                                                hasMore = false,
                                                remainingChats = 0,
                                            )
                                            chattingHistories[event.question] = createdQuestionHistory
                                            matchingExperiencesList[event.question] = emptyList()
                                            reduce {
                                                copy(
                                                    questions = questions + event.question,
                                                    currentQuestion = event.question,
                                                    chattingHistory = createdQuestionHistory,
                                                    matchingExperiences = emptyList(),
                                                )
                                            }
                                        }
                                    }
                                }
                                is ChatScreen.Event.RefreshData -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        val preferredQuestionId = pendingCreatedQuestionId
                                            ?: event.preferredQuestionId
                                            ?: currentQuestion.id
                                        scope.launch {
                                            questionRepository.getQuestions(screen.projectId).onSuccess { latestQuestions ->
                                                if (latestQuestions.isEmpty()) return@onSuccess

                                                var hasError = false
                                                val latestMatching = hashMapOf<Question, List<MatchingExperience>>()
                                                val latestHistories = hashMapOf<Question, ChattingHistory>()

                                                latestQuestions.fastMap { question ->
                                                    async {
                                                        experienceRepository.getMatchingExperiences(question.id)
                                                            .onSuccess { matching ->
                                                                latestMatching[question] = matching
                                                            }
                                                            .onFailure {
                                                                hasError = true
                                                                Log.e(TAG, "refresh getMatchingExperiences failed: $it")
                                                            }
                                                    }
                                                }.awaitAll()

                                                if (hasError) return@onSuccess

                                                latestQuestions.fastMap { question ->
                                                    async {
                                                        chattingRepository.getChatHistory(question.id)
                                                            .onSuccess { history ->
                                                                latestHistories[question] = history
                                                            }
                                                            .onFailure {
                                                                hasError = true
                                                                Log.e(TAG, "refresh getChatHistory failed: $it")
                                                            }
                                                    }
                                                }.awaitAll()

                                                if (hasError) return@onSuccess

                                                chattingHistories.clear()
                                                chattingHistories.putAll(latestHistories)
                                                matchingExperiencesList.clear()
                                                matchingExperiencesList.putAll(latestMatching)

                                                val nextQuestion = latestQuestions.firstOrNull { it.id == preferredQuestionId }
                                                    ?: latestQuestions.firstOrNull { it.id == currentQuestion.id }
                                                    ?: return@onSuccess
                                                val nextHistory = latestHistories[nextQuestion] ?: return@onSuccess
                                                val nextMatching = latestMatching[nextQuestion] ?: return@onSuccess

                                                if (pendingCreatedQuestionId == nextQuestion.id) {
                                                    pendingCreatedQuestionId = null
                                                }

                                                reduce {
                                                    copy(
                                                        questions = latestQuestions,
                                                        currentQuestion = nextQuestion,
                                                        chattingHistory = nextHistory,
                                                        matchingExperiences = nextMatching
                                                    )
                                                }
                                            }.onFailure {
                                                Log.e(TAG, "refresh getQuestions failed: $it")
                                            }
                                        }
                                    }
                                }
                                is ChatScreen.Event.EditQuestions -> {
                                    editQuestionsNavigator.goTo(screenProvider.editQuestionsScreen(screen.projectId))
                                }
                                is ChatScreen.Event.AddQuestion -> {
                                    newQuestionNavigator.goTo(screenProvider.newQuestionScreen(screen.projectId))
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
                                        pendingCreatedQuestionId = null
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
                                                            streamingStatus = ChattingStreamingStatus.Loading,
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
                                        val updatedQuestion = currentQuestion.copy(letter = event.letter)
                                        val updatedHistories = hashMapOf<Question, ChattingHistory>()
                                        chattingHistories.forEach { (question, history) ->
                                            updatedHistories[if (question.id == updatedQuestion.id) updatedQuestion else question] = history
                                        }
                                        val updatedMatchingExperiences = hashMapOf<Question, List<MatchingExperience>>()
                                        matchingExperiencesList.forEach { (question, matching) ->
                                            updatedMatchingExperiences[if (question.id == updatedQuestion.id) updatedQuestion else question] = matching
                                        }
                                        chattingHistories.clear()
                                        chattingHistories.putAll(updatedHistories)
                                        matchingExperiencesList.clear()
                                        matchingExperiencesList.putAll(updatedMatchingExperiences)
                                        reduce {
                                            copy(
                                                currentQuestion = updatedQuestion,
                                                questions = questions.map {
                                                    if (it.id == updatedQuestion.id) updatedQuestion else it
                                                }
                                            )
                                        }
                                    }
                                }
                                is ChatScreen.Event.SaveLetter -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        scope.launch {
                                            val savingQuestion = currentQuestion.copy(letter = event.letter)
                                            questionRepository.updateQuestion(
                                                projectId = screen.projectId,
                                                question = savingQuestion
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
                                is ChatScreen.Event.CompleteQuestion -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        scope.launch {
                                            questionRepository.completeQuestion(
                                                projectId = screen.projectId,
                                                questionId = currentQuestion.id
                                            ).onSuccess {
                                                val completedQuestion = currentQuestion.copy(isCompleted = true)
                                                val updatedHistories = hashMapOf<Question, ChattingHistory>()
                                                chattingHistories.forEach { (question, history) ->
                                                    updatedHistories[
                                                        if (question.id == completedQuestion.id) completedQuestion else question
                                                    ] = history
                                                }
                                                val updatedMatchingExperiences = hashMapOf<Question, List<MatchingExperience>>()
                                                matchingExperiencesList.forEach { (question, matching) ->
                                                    updatedMatchingExperiences[
                                                        if (question.id == completedQuestion.id) completedQuestion else question
                                                    ] = matching
                                                }
                                                chattingHistories.clear()
                                                chattingHistories.putAll(updatedHistories)
                                                matchingExperiencesList.clear()
                                                matchingExperiencesList.putAll(updatedMatchingExperiences)

                                                reduce {
                                                    copy(
                                                        currentQuestion = completedQuestion,
                                                        questions = questions.map {
                                                            if (it.id == completedQuestion.id) completedQuestion else it
                                                        },
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                is ChatScreen.Event.TryDeleteProject -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showDeleteDialog = true)
                                        }
                                    }
                                }
                                is ChatScreen.Event.DismissDeleteDialog -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showDeleteDialog = false)
                                        }
                                    }
                                }
                                is ChatScreen.Event.ConfirmDeleteProject -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showDeleteDialog = false)
                                        }
                                    }
                                    scope.launch {
                                        projectRepository.deleteProject(screen.projectId)
                                            .onSuccess {
                                                withContext(Dispatchers.Main.immediate) {
                                                    navigator.pop(
                                                        result = ChatScreen.ProjectDeletedResult(
                                                            projectId = screen.projectId
                                                        )
                                                    )
                                                }
                                            }
                                            .onFailure {
                                                Log.e(TAG, "deleteProject failed: $it")
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
                                is ChatScreen.Event.GenerateDraft -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        val currentChatHistory = chattingHistories[currentQuestion]
                                        val updatedChatHistory =
                                            currentChatHistory?.copy(experienceIds = event.experienceIds.toSet())
                                                ?: chattingHistory.copy(experienceIds = event.experienceIds.toSet())
                                        chattingHistories[currentQuestion] = updatedChatHistory

                                        reduce {
                                            copy(
                                                showExperienceModal = false,
                                                chattingHistory = updatedChatHistory
                                            )
                                        }

                                        scope.launch {
                                            chattingRepository.startChattingStream(
                                                questionId = currentQuestion.id,
                                                sendingMessage = DRAFT_GENERATION_MESSAGE,
                                                experienceIds = event.experienceIds
                                            ).onStart {
                                                value.runOn<ChatScreen.State.Success> {
                                                    reduce {
                                                        copy(
                                                            streamingStatus = ChattingStreamingStatus.Loading,
                                                            userInput = "",
                                                            chattingHistory = updatedChatHistory.copy(
                                                                chattings = updatedChatHistory.chattings + ChattingContent.User(
                                                                    id = LocalDateTime.now().toString(),
                                                                    message = DRAFT_GENERATION_MESSAGE,
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
                                is ChatScreen.Event.TryUploadExperience -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showExperienceModal = true)
                                        }
                                    }
                                }
                                is ChatScreen.Event.ClickAddExperience -> {
                                    value.runOn<ChatScreen.State.Success> {
                                        reduce {
                                            copy(showExperienceModal = false)
                                        }
                                    }
                                    navigator.goTo(screenProvider.experienceCreateScreen())
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
                Log.e(TAG, "getQuestions failed: $it")
                reduce { ChatScreen.State.LoadFailed(eventSink = loadingOrFailedEventSink) }
            }
        }

        LaunchedEffect(editedQuestionsResult) {
            val result = editedQuestionsResult
            if (result != null) {
                state.runOn<ChatScreen.State.Success> {
                    eventSink(ChatScreen.Event.ApplyEditedQuestions(result.questions))
                    eventSink(ChatScreen.Event.RefreshData())
                }
                editedQuestionsResult = null
            }
        }
        LaunchedEffect(createdQuestionResult) {
            val result = createdQuestionResult
            if (result != null) {
                state.runOn<ChatScreen.State.Success> {
                    val createdQuestion = Question(
                        id = result.questionId,
                        title = result.title,
                        maxLength = result.maxLength,
                        letter = result.letter,
                    )
                    pendingCreatedQuestionId = result.questionId
                    eventSink(ChatScreen.Event.AddCreatedQuestion(createdQuestion))
                    eventSink(ChatScreen.Event.RefreshData(preferredQuestionId = result.questionId))
                }
                createdQuestionResult = null
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

    companion object {
        private val TAG = ChatPresenter::class.simpleName
        private const val DRAFT_GENERATION_MESSAGE = "선택된 경험 바탕으로 자기소개서 초안 작성해줘"
    }
}

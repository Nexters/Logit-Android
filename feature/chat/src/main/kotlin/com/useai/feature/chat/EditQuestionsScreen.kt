package com.useai.feature.chat

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.QuestionRepository
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.component.snackbar.showLogitSnackbar
import com.useai.core.model.chat.Question
import com.useai.core.model.project.ProjectQuestionParam
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditQuestionsScreen(
    val projectId: String,
) : Screen {
    @Parcelize
    data class EditedQuestionResult(
        val id: String,
        val title: String,
        val maxLength: Int,
        val letter: String,
    ) : android.os.Parcelable

    @Parcelize
    data class QuestionsEditedResult(
        val questions: List<EditedQuestionResult>,
    ) : PopResult

    data class EditableQuestion(
        val id: String,
        val title: String,
        val maxLength: Int,
        val letter: String,
    )

    data class State(
        val questions: List<EditableQuestion>,
        val isSubmitting: Boolean,
        val isSubmitEnabled: Boolean,
        val showValidationErrors: Boolean,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class ChangeQuestion(val index: Int, val value: String) : Event
        data class ChangeMaxLength(val index: Int, val value: String) : Event
        data object AddQuestion : Event
        data class DeleteQuestion(val index: Int) : Event
        data object Submit : Event
    }
}

class EditQuestionsPresenter @AssistedInject constructor(
    @Assisted private val screen: EditQuestionsScreen,
    @Assisted private val navigator: Navigator,
    private val questionRepository: QuestionRepository,
) : Presenter<EditQuestionsScreen.State> {

    @Composable
    override fun present(): EditQuestionsScreen.State {
        val scope = rememberCoroutineScope()
        val snackbarHostState = LocalLogitSnackbarHostState.current

        val invalidInputMessage = stringResource(R.string.chat_edit_questions_invalid)
        val saveSuccessMessage = stringResource(R.string.chat_edit_questions_success)
        val saveFailedMessage = stringResource(R.string.chat_edit_questions_failed)
        val minCountMessage = stringResource(R.string.chat_edit_questions_min_count)
        val maxCountMessage = stringResource(R.string.chat_edit_questions_max_count)

        var questions by rememberRetained { mutableStateOf<List<EditQuestionsScreen.EditableQuestion>>(emptyList()) }
        var initialQuestionIds by rememberRetained { mutableStateOf<Set<String>>(emptySet()) }
        var isSubmitting by rememberRetained { mutableStateOf(false) }
        var showValidationErrors by rememberRetained { mutableStateOf(false) }
        var isLoaded by rememberRetained { mutableStateOf(false) }

        LaunchedEffect(screen.projectId, isLoaded) {
            if (isLoaded) return@LaunchedEffect

            questionRepository.getQuestions(screen.projectId)
                .onSuccess { loaded ->
                    questions = if (loaded.isEmpty()) {
                        listOf(
                            EditQuestionsScreen.EditableQuestion(
                                id = "",
                                title = "",
                                maxLength = 0,
                                letter = "",
                            )
                        )
                    } else {
                        loaded.map {
                            EditQuestionsScreen.EditableQuestion(
                                id = it.id,
                                title = it.title,
                                maxLength = it.maxLength,
                                letter = it.letter,
                            )
                        }
                    }
                    initialQuestionIds = loaded.map { it.id }.toSet()
                    isLoaded = true
                }
                .onFailure {
                    Log.e(TAG, "getQuestions failed: $it")
                    snackbarHostState.showLogitSnackbar(
                        message = saveFailedMessage,
                        iconResId = R.drawable.ic_alert,
                    )
                    navigator.pop()
                }
        }

        return EditQuestionsScreen.State(
            questions = questions,
            isSubmitting = isSubmitting,
            isSubmitEnabled = !isSubmitting && questions.isNotEmpty() && questions.all {
                it.title.trim().isNotBlank() && it.maxLength > 0
            },
            showValidationErrors = showValidationErrors,
        ) { event ->
            when (event) {
                EditQuestionsScreen.Event.Back -> navigator.pop()

                is EditQuestionsScreen.Event.ChangeQuestion -> {
                    showValidationErrors = false
                    questions = questions.mapIndexed { index, question ->
                        if (index == event.index) question.copy(title = event.value) else question
                    }
                }

                is EditQuestionsScreen.Event.ChangeMaxLength -> {
                    showValidationErrors = false
                    val parsed = event.value.toIntOrNull() ?: 0
                    questions = questions.mapIndexed { index, question ->
                        if (index == event.index) question.copy(maxLength = parsed) else question
                    }
                }

                EditQuestionsScreen.Event.AddQuestion -> {
                    if (questions.size >= MAX_QUESTION_COUNT) {
                        scope.launch {
                            snackbarHostState.showLogitSnackbar(
                                message = maxCountMessage,
                                iconResId = R.drawable.ic_alert,
                            )
                        }
                    } else {
                        questions = questions + EditQuestionsScreen.EditableQuestion(
                            id = "",
                            title = "",
                            maxLength = 0,
                            letter = "",
                        )
                    }
                }

                is EditQuestionsScreen.Event.DeleteQuestion -> {
                    if (questions.size <= MIN_QUESTION_COUNT) {
                        scope.launch {
                            snackbarHostState.showLogitSnackbar(
                                message = minCountMessage,
                                iconResId = R.drawable.ic_alert,
                            )
                        }
                    } else {
                        questions = questions.filterIndexed { index, _ -> index != event.index }
                    }
                }

                EditQuestionsScreen.Event.Submit -> {
                    if (!isSubmitting) {
                        val normalized = questions.map {
                            it.copy(title = it.title.trim())
                        }

                        val hasInvalidQuestion = normalized.any { it.title.isBlank() || it.maxLength <= 0 }
                        if (hasInvalidQuestion) {
                            showValidationErrors = true
                            scope.launch {
                                snackbarHostState.showLogitSnackbar(
                                    message = invalidInputMessage,
                                    iconResId = R.drawable.ic_alert,
                                )
                            }
                        } else {
                            isSubmitting = true
                            scope.launch {
                                val remainingIds = normalized.mapNotNull { it.id.takeIf(String::isNotBlank) }.toSet()
                                val deletedIds = initialQuestionIds - remainingIds
                                val savedQuestions = mutableListOf<EditQuestionsScreen.EditedQuestionResult>()

                                for (questionId in deletedIds) {
                                    val deleteResult = questionRepository.deleteQuestion(
                                        projectId = screen.projectId,
                                        questionId = questionId,
                                    )
                                    if (deleteResult.isFailure) {
                                        Log.e(TAG, "deleteQuestion failed: ${deleteResult.exceptionOrNull()}")
                                        snackbarHostState.showLogitSnackbar(
                                            message = saveFailedMessage,
                                            iconResId = R.drawable.ic_alert,
                                        )
                                        isSubmitting = false
                                        return@launch
                                    }
                                }

                                for (question in normalized) {
                                    if (question.id.isBlank()) {
                                        val createResult = questionRepository.createQuestion(
                                            projectId = screen.projectId,
                                            question = ProjectQuestionParam(
                                                question = question.title,
                                                maxLength = question.maxLength,
                                            )
                                        )
                                        val createdId = createResult.getOrNull()
                                        if (createdId == null) {
                                            Log.e(TAG, "createQuestion failed: ${createResult.exceptionOrNull()}")
                                            snackbarHostState.showLogitSnackbar(
                                                message = saveFailedMessage,
                                                iconResId = R.drawable.ic_alert,
                                            )
                                            isSubmitting = false
                                            return@launch
                                        }
                                        savedQuestions += EditQuestionsScreen.EditedQuestionResult(
                                            id = createdId,
                                            title = question.title,
                                            maxLength = question.maxLength,
                                            letter = question.letter,
                                        )
                                    } else {
                                        val updateResult = questionRepository.updateQuestion(
                                            projectId = screen.projectId,
                                            question = Question(
                                                id = question.id,
                                                title = question.title,
                                                maxLength = question.maxLength,
                                                letter = question.letter,
                                            )
                                        )
                                        if (updateResult.isFailure) {
                                            Log.e(TAG, "updateQuestion failed: ${updateResult.exceptionOrNull()}")
                                            snackbarHostState.showLogitSnackbar(
                                                message = saveFailedMessage,
                                                iconResId = R.drawable.ic_alert,
                                            )
                                            isSubmitting = false
                                            return@launch
                                        }
                                        savedQuestions += EditQuestionsScreen.EditedQuestionResult(
                                            id = question.id,
                                            title = question.title,
                                            maxLength = question.maxLength,
                                            letter = question.letter,
                                        )
                                    }
                                }

                                snackbarHostState.showLogitSnackbar(
                                    message = saveSuccessMessage,
                                    iconResId = R.drawable.ic_alert,
                                )
                                isSubmitting = false
                                navigator.pop(EditQuestionsScreen.QuestionsEditedResult(savedQuestions))
                            }
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(EditQuestionsScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: EditQuestionsScreen,
            navigator: Navigator,
        ): EditQuestionsPresenter
    }

    companion object {
        private val TAG = EditQuestionsPresenter::class.simpleName
        private const val MIN_QUESTION_COUNT = 1
        private const val MAX_QUESTION_COUNT = 10
    }
}

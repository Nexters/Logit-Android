package com.useai.feature.projects

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.data.repository.QuestionRepository
import com.useai.core.model.chat.Question
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectLibraryScreen(val projectId: String) : Screen {
    sealed interface State : CircuitUiState {
        data class Loading(
            val eventSink: (Event) -> Unit = {},
        ) : State

        data class LoadFailed(
            val eventSink: (Event) -> Unit = {},
        ) : State

        data class Success(
            val projectTitle: String,
            val questions: List<Question>,
            val selectedQuestionId: String,
            val isMenuExpanded: Boolean,
            val showDeleteDialog: Boolean,
            val eventSink: (Event) -> Unit,
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class SelectQuestion(val questionId: String) : Event
        data object ToggleMenu : Event
        data object DismissMenu : Event
        data object EditProject : Event
        data object TryDeleteQuestion : Event
        data object DismissDeleteDialog : Event
        data object ConfirmDeleteQuestion : Event
    }
}

class ProjectLibraryPresenter @AssistedInject constructor(
    @Assisted private val screen: ProjectLibraryScreen,
    @Assisted private val navigator: Navigator,
    private val projectRepository: ProjectRepository,
    private val questionRepository: QuestionRepository,
) : Presenter<ProjectLibraryScreen.State> {

    @Composable
    override fun present(): ProjectLibraryScreen.State {
        val scope = rememberStableCoroutineScope()
        val screenProvider = LocalScreenProvider.current
        var shouldRefreshAfterEdit by rememberRetained { mutableStateOf(false) }

        var state by rememberRetained {
            mutableStateOf<ProjectLibraryScreen.State>(ProjectLibraryScreen.State.Loading())
        }
        var loaded by rememberRetained { mutableStateOf(false) }

        if (!loaded) {
            loaded = true
            scope.launch {
                val project = projectRepository.getProject(screen.projectId).getOrNull()
                val questions = questionRepository.getQuestions(screen.projectId).getOrNull()

                if (project == null || questions == null || questions.isEmpty()) {
                    Log.e(TAG, "failed to load project library. project=$project questions=${questions?.size}")
                    state = ProjectLibraryScreen.State.LoadFailed(
                        eventSink = { event ->
                            if (event is ProjectLibraryScreen.Event.Back) navigator.pop()
                        }
                    )
                    return@launch
                }

                state = ProjectLibraryScreen.State.Success(
                    projectTitle = "${project.company} ${project.jobPosition}",
                    questions = questions,
                    selectedQuestionId = questions.first().id,
                    isMenuExpanded = false,
                    showDeleteDialog = false,
                    eventSink = eventSink@{ event ->
                        when (event) {
                            ProjectLibraryScreen.Event.Back -> navigator.pop()
                            is ProjectLibraryScreen.Event.SelectQuestion -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(
                                    selectedQuestionId = event.questionId,
                                    isMenuExpanded = false,
                                )
                            }
                            ProjectLibraryScreen.Event.ToggleMenu -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(isMenuExpanded = !current.isMenuExpanded)
                            }
                            ProjectLibraryScreen.Event.DismissMenu -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(isMenuExpanded = false)
                            }
                            ProjectLibraryScreen.Event.EditProject -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(isMenuExpanded = false)
                                shouldRefreshAfterEdit = true
                                navigator.goTo(screenProvider.editQuestionsScreen(screen.projectId))
                            }
                            ProjectLibraryScreen.Event.TryDeleteQuestion -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(
                                    isMenuExpanded = false,
                                    showDeleteDialog = true,
                                )
                            }
                            ProjectLibraryScreen.Event.DismissDeleteDialog -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(showDeleteDialog = false)
                            }
                            ProjectLibraryScreen.Event.ConfirmDeleteQuestion -> {
                                val current = state as? ProjectLibraryScreen.State.Success ?: return@eventSink
                                state = current.copy(showDeleteDialog = false)

                                if (current.questions.size <= 1) {
                                    Log.w(TAG, "skip delete selected question: at least one question is required")
                                    return@eventSink
                                }

                                scope.launch {
                                    questionRepository.deleteQuestion(
                                        projectId = screen.projectId,
                                        questionId = current.selectedQuestionId
                                    ).onSuccess {
                                        questionRepository.getQuestions(screen.projectId)
                                            .onSuccess { latestQuestions ->
                                                if (latestQuestions.isEmpty()) return@onSuccess
                                                val selectedQuestionId = latestQuestions.first().id
                                                val latestState =
                                                    state as? ProjectLibraryScreen.State.Success ?: return@onSuccess
                                                state = latestState.copy(
                                                    questions = latestQuestions,
                                                    selectedQuestionId = selectedQuestionId,
                                                    isMenuExpanded = false,
                                                    showDeleteDialog = false,
                                                )
                                            }
                                            .onFailure {
                                                Log.e(TAG, "refresh questions failed after delete: $it")
                                            }
                                    }.onFailure {
                                        Log.e(TAG, "delete question failed: $it")
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        LaunchedEffect(shouldRefreshAfterEdit) {
            if (!shouldRefreshAfterEdit) return@LaunchedEffect
            val current = state as? ProjectLibraryScreen.State.Success
            if (current == null) {
                return@LaunchedEffect
            }

            questionRepository.getQuestions(screen.projectId)
                .onSuccess { latestQuestions ->
                    if (latestQuestions.isNotEmpty()) {
                        val selectedQuestionId = latestQuestions.firstOrNull {
                            it.id == current.selectedQuestionId
                        }?.id ?: latestQuestions.first().id
                        state = current.copy(
                            questions = latestQuestions,
                            selectedQuestionId = selectedQuestionId,
                            showDeleteDialog = false,
                        )
                    }
                    shouldRefreshAfterEdit = false
                }
                .onFailure {
                    Log.e(TAG, "refresh questions failed after edit: $it")
                    shouldRefreshAfterEdit = false
                }
        }

        return state
    }

    @AssistedFactory
    @CircuitInject(ProjectLibraryScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: ProjectLibraryScreen,
            navigator: Navigator,
        ): ProjectLibraryPresenter
    }

    companion object {
        private val TAG = ProjectLibraryPresenter::class.simpleName
    }
}

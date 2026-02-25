package com.useai.feature.projects

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.model.project.ProjectListItem
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object ProjectsScreen : Screen {
    data class State(
        val projects: List<ProjectListItem>,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NewProjectClicked : Event
        data class ProjectClicked(
            val projectId: String,
        ) : Event
    }
}

class ProjectsPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val projectRepository: ProjectRepository,
) : Presenter<ProjectsScreen.State> {
    @Composable
    override fun present(): ProjectsScreen.State {
        val projects by produceState(initialValue = emptyList()) {
            projectRepository.getProjects() // TODO: 페이징 사용, 화면 진입 시마다 요청하지 않도록 개선 필요
                .onSuccess { value = it }
                .onFailure {
                    Log.e(TAG, "getProjects failed: $it")
                }
        }
        val screenProvider = LocalScreenProvider.current

        return ProjectsScreen.State(
            projects = projects,
        ) { event ->
            when (event) {
                ProjectsScreen.Event.NewProjectClicked -> {
                    navigator.goTo(screenProvider.newProjectBasicInfoScreen())
                }

                is ProjectsScreen.Event.ProjectClicked -> {
                    navigator.goTo(screenProvider.projectLibraryScreen(event.projectId))
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(ProjectsScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ProjectsPresenter
    }

    companion object {
        private val TAG = ProjectsPresenter::class.simpleName
    }
}

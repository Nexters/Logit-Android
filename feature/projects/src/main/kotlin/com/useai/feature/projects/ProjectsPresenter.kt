package com.useai.feature.projects

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.ui.LocalTabScrollState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class ProjectsPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val projectRepository: ProjectRepository,
) : Presenter<ProjectsScreen.State> {
    @Composable
    override fun present(): ProjectsScreen.State {
        val scrollState = LocalTabScrollState.current[ProjectsScreen] ?: rememberRetained { LazyListState() }
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
            scrollState = scrollState,
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

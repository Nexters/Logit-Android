package com.useai.feature.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.AccountRepository
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.model.account.UserProfile
import com.useai.core.model.project.ProjectListItem
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.ui.ExperienceType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val userProfile: UserProfile,
        val bannerItems: List<ExperienceType>,
        val projects: List<ProjectListItem>,
        val openedProjectMenuId: String?,
        val showProjectDeleteDialog: Boolean,
        val isDeletingProject: Boolean,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NewProjectClicked : Event
        data class ProjectClicked(val projectId: String) : Event
        data class ProjectMoreClicked(val projectId: String) : Event
        data object DismissProjectMenu : Event
        data class DeleteProjectClicked(val projectId: String) : Event
        data class DeleteProjectConfirmed(val projectId: String) : Event
        data object DeleteProjectCanceled : Event
        data object AccountClicked : Event
    }
}

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val accountRepository: AccountRepository,
    private val projectRepository: ProjectRepository,
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        val scope = rememberStableCoroutineScope()
        val lifecycleOwner = LocalLifecycleOwner.current
        val userProfile by produceRetainedState(initialValue = UserProfile("", "")) {
            accountRepository.getUser()
                .onSuccess {
                    value = UserProfile(it.fullName, it.profileImageUrl)
                }
                .onFailure {
                    Log.e(TAG, "getUser failed: $it")
                }
        }
        var projects by rememberRetained { mutableStateOf<List<ProjectListItem>>(emptyList()) }
        var openedProjectMenuId by rememberRetained { mutableStateOf<String?>(null) }
        var showProjectDeleteDialog by rememberRetained { mutableStateOf(false) }
        var isDeletingProject by rememberRetained { mutableStateOf(false) }
        val screenProvider = LocalScreenProvider.current

        fun fetchProjects() {
            scope.launch {
                projectRepository.getProjects() // TODO: paging
                    .onSuccess {
                        projects = it
                        openedProjectMenuId = null
                        isDeletingProject = false
                    }
                    .onFailure {
                        Log.e(TAG, "getProjects failed: $it")
                    }
            }
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    fetchProjects()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        return HomeScreen.State(
            userProfile = userProfile,
            bannerItems = ExperienceType.entries,
            projects = projects,
            openedProjectMenuId = openedProjectMenuId,
            showProjectDeleteDialog = showProjectDeleteDialog,
            isDeletingProject = isDeletingProject,
        ) { event ->
            when (event) {
                HomeScreen.Event.NewProjectClicked -> navigator.goTo(
                    screenProvider.newProjectBasicInfoScreen()
                )

                is HomeScreen.Event.ProjectClicked -> navigator.goTo(
                    screenProvider.chatScreen(event.projectId)
                )

                is HomeScreen.Event.ProjectMoreClicked -> {
                    openedProjectMenuId = if (openedProjectMenuId == event.projectId) {
                        null
                    } else {
                        event.projectId
                    }
                }

                HomeScreen.Event.DismissProjectMenu -> {
                    openedProjectMenuId = null
                }

                is HomeScreen.Event.DeleteProjectClicked -> {
                    showProjectDeleteDialog = true
                }

                is HomeScreen.Event.DeleteProjectConfirmed -> {
                    openedProjectMenuId = null
                    isDeletingProject = true
                    showProjectDeleteDialog = false
                    scope.launch {
                        projectRepository.deleteProject(event.projectId)
                            .onSuccess { fetchProjects() }
                            .onFailure {
                                isDeletingProject = false
                                Log.e(TAG, "deleteProject failed: $it")
                            }
                    }
                }

                HomeScreen.Event.DeleteProjectCanceled -> {
                    openedProjectMenuId = null
                    showProjectDeleteDialog = false
                }

                HomeScreen.Event.AccountClicked -> navigator.goTo(
                    screenProvider.accountScreen()
                )
            }
        }
    }

    @AssistedFactory
    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): HomePresenter
    }

    companion object {
        private val TAG = HomePresenter::class.simpleName
    }
}

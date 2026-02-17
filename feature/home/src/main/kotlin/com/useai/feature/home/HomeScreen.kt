package com.useai.feature.home

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
import com.useai.core.ui.ExperienceBannerItem
import com.useai.core.ui.ExperienceType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val bannerItems: List<ExperienceBannerItem>,
        val projects: List<ProjectListItem>,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NewProjectClicked : Event
        data class ProjectClicked(val projectId: String) : Event
        data object AccountClicked : Event
    }
}

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val projectRepository: ProjectRepository,
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        val dummyBannerItems = listOf(
            ExperienceBannerItem(
                experienceType = ExperienceType.Leadership,
                experienceCount = 7,
            ),
            ExperienceBannerItem(
                experienceType = ExperienceType.Expertise,
                experienceCount = 1,
            ),
            ExperienceBannerItem(
                experienceType = ExperienceType.Analysis,
                experienceCount = 3,
            ),
            ExperienceBannerItem(
                experienceType = ExperienceType.Creativity,
                experienceCount = 30,
            ),
        )
        val projects by produceState(initialValue = emptyList()) {
            projectRepository.getProjects() // TODO: 페이징 사용, 화면 진입 시마다 요청하지 않도록 개선 필요
                .onSuccess { value = it }
                .onFailure {
                    Log.e(TAG, "getProjects failed: $it")
                }
        }
        val screenProvider = LocalScreenProvider.current

        return HomeScreen.State(
            bannerItems = dummyBannerItems,
            projects = projects
        ) { event ->
            when (event) {
                HomeScreen.Event.NewProjectClicked -> navigator.goTo(
                    screenProvider.newProjectBasicInfoScreen()
                )

                is HomeScreen.Event.ProjectClicked -> navigator.goTo(
                    screenProvider.chatScreen(
                        event.projectId
                    )
                )

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

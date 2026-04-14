package com.useai.logit

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.BackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringResultNavigator
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.ExperimentalCircuitApi
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.feature.home.HomeScreen
import com.useai.feature.projects.ProjectsScreen
import com.useai.feature.experience.ExperienceScreen
import com.useai.feature.report.ReportScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object RootScreen : Screen {
    data class RootUiState(
        val backStack: BackStack<out BackStack.Record>,
        val navigator: Navigator,
        val scrollStates: Map<Screen, LazyListState>,
        val eventSink: (RootEvent) -> Unit,
    ) : CircuitUiState

    sealed interface RootEvent : CircuitUiEvent {
        data class ChangeScreen(val screen: Screen) : RootEvent
    }
}

class RootPresenter @AssistedInject constructor(
    @Assisted private val parentNavigator: Navigator,
) : Presenter<RootScreen.RootUiState> {
    @OptIn(ExperimentalCircuitApi::class)
    @Composable
    override fun present(): RootScreen.RootUiState {
        val backStack = rememberSaveableBackStack(HomeScreen)
        val baseNavigator = rememberCircuitNavigator(backStack) {
            parentNavigator.pop()
        }
        val navigator = rememberAnsweringResultNavigator(baseNavigator, backStack)

        val homeScrollState = rememberRetained { LazyListState() }
        val projectsScrollState = rememberRetained { LazyListState() }
        val experienceScrollState = rememberRetained { LazyListState() }
        val reportScrollState = rememberRetained { LazyListState() }

        val scrollStates = remember {
            mapOf(
                HomeScreen to homeScrollState,
                ProjectsScreen to projectsScrollState,
                ExperienceScreen to experienceScrollState,
                ReportScreen to reportScrollState
            )
        }

        return RootScreen.RootUiState(
            backStack = backStack,
            navigator = navigator,
            scrollStates = scrollStates
        ) { event ->
            when (event) {
                is RootScreen.RootEvent.ChangeScreen -> {
                    val currentScreen = backStack.topRecord?.screen
                    if (currentScreen != event.screen) {
                        navigator.resetRoot(event.screen)
                    }
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(RootScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(navigator: Navigator): RootPresenter
    }
}

package com.useai.logit

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
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
import com.useai.core.data.repository.TokenRepository
import com.useai.feature.experience.ExperienceScreen
import com.useai.feature.home.HomeScreen
import com.useai.feature.projects.ProjectsScreen
import com.useai.feature.report.ReportScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration.Companion.milliseconds

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
    private val tokenRepository: TokenRepository,
) : Presenter<RootScreen.RootUiState> {
    @OptIn(ExperimentalCircuitApi::class)
    @Composable
    override fun present(): RootScreen.RootUiState {
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(lifecycleOwner, tokenRepository) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    tokenRepository.getTokenBalance()
                        .onFailure {
                            Log.e(TAG, "Failed to refresh token grants", it)
                        }
                    delay(TOKEN_GRANT_REFRESH_INTERVAL_MILLIS.milliseconds)
                }
            }
        }

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

    companion object {
        private val TAG = RootPresenter::class.simpleName
        private const val TOKEN_GRANT_REFRESH_INTERVAL_MILLIS = 30_000L
    }
}

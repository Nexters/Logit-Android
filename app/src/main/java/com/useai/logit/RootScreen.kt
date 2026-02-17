package com.useai.logit

import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.BackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberAnsweringResultNavigator
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.ExperimentalCircuitApi
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.feature.chat.ChatScreen
import com.useai.feature.home.HomeScreen
import com.useai.feature.newproject.NewProjectBasicInfoScreen
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

        return RootScreen.RootUiState(
            backStack = backStack,
            navigator = navigator
        ) { event ->
            when (event) {
                is RootScreen.RootEvent.ChangeScreen -> {
                    val currentScreen = backStack.topRecord?.screen
                    if (currentScreen != event.screen) {
                        while (backStack.size > 0) {
                            backStack.pop()
                        }

                        if (currentScreen != null &&
                            (event.screen is ChatScreen || event.screen is NewProjectBasicInfoScreen)
                        ) {
                            backStack.push(currentScreen)
                        }
                        backStack.push(event.screen)
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

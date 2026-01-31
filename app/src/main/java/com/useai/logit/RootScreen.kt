package com.useai.logit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.feature.home.HomeScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object RootScreen : Screen {
    data class RootUiState(
        val displayedScreen: Screen,
        val canPop: Boolean, // 백스택이 존재하여 뒤로 갈 수 있는지 여부
        val eventSink: (RootEvent) -> Unit,
    ) : CircuitUiState

    sealed interface RootEvent : CircuitUiEvent {
        data class ChangeScreen(val screen: Screen) : RootEvent
        data class NestedNavEvent(val navEvent: NavEvent) : RootEvent
    }
}

class RootPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RootScreen.RootUiState> {
    @Composable
    override fun present(): RootScreen.RootUiState {
        var displayedScreen by rememberRetained { mutableStateOf<Screen>(HomeScreen) }
        var screenStack by rememberRetained { mutableStateOf(listOf<Screen>()) }

        return RootScreen.RootUiState(
            displayedScreen = displayedScreen,
            canPop = screenStack.isNotEmpty()
        ) { event ->
            when (event) {
                is RootScreen.RootEvent.NestedNavEvent -> {
                    when (val navEvent = event.navEvent) {
                        is NavEvent.Pop -> {
                            if (screenStack.isNotEmpty()) {
                                displayedScreen = screenStack.last()
                                screenStack = screenStack.dropLast(1)
                            } else {
                                navigator.pop()
                            }
                        }
                        is NavEvent.GoTo -> {
                            if (displayedScreen != navEvent.screen) {
                                screenStack = screenStack.filter { it != navEvent.screen } + displayedScreen
                                displayedScreen = navEvent.screen
                            }
                        }
                        else -> navigator.onNavEvent(event.navEvent)
                    }
                }
                is RootScreen.RootEvent.ChangeScreen -> {
                    if (displayedScreen != event.screen) {
                        screenStack = screenStack.filter { it != event.screen } + displayedScreen
                        displayedScreen = event.screen
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

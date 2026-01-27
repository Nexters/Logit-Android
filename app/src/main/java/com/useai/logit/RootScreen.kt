package com.useai.logit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
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
data object RootScreen : Screen

class RootPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<RootUiState> {
    @Composable
    override fun present(): RootUiState {
        var displayedScreen by remember { mutableStateOf<Screen>(HomeScreen) }

        return RootUiState(displayedScreen = displayedScreen) { event ->
            when (event) {
                is RootEvent.NestedNavEvent -> navigator.onNavEvent(event.navEvent)
                is RootEvent.ChangeScreen -> displayedScreen = event.screen
            }
        }
    }

    @AssistedFactory
    @CircuitInject(RootScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(navigator: Navigator): RootPresenter
    }
}

data class RootUiState(
    val displayedScreen: Screen,
    val eventSink: (RootEvent) -> Unit,
) : CircuitUiState


sealed interface RootEvent {
    data class ChangeScreen(val screen: Screen) : RootEvent
    data class NestedNavEvent(val navEvent: NavEvent) : RootEvent
}

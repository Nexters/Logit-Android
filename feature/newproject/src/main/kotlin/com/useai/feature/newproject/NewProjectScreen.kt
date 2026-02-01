package com.useai.feature.newproject

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object NewProjectScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
    }
}

class NewProjectPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NewProjectScreen.State> {
    @Composable
    override fun present(): NewProjectScreen.State {
        return NewProjectScreen.State { event ->
            when (event) {
                NewProjectScreen.Event.Back -> navigator.pop()
            }
        }
    }

    @AssistedFactory
    @CircuitInject(NewProjectScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): NewProjectPresenter
    }
}

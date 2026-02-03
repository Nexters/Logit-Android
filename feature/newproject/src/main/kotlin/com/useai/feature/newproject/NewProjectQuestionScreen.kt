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
data class NewProjectQuestionScreen(
    val companyName: String,
    val jobName: String,
) : Screen {
    data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
    }
}

class NewProjectQuestionPresenter @AssistedInject constructor(
    @Assisted private val screen: NewProjectQuestionScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<NewProjectQuestionScreen.State> {
    @Composable
    override fun present(): NewProjectQuestionScreen.State {
        val companyName = screen.companyName
        val jobName = screen.jobName

        return NewProjectQuestionScreen.State {
            when (it) {
                NewProjectQuestionScreen.Event.Back -> navigator.pop()
            }
        }
    }

    @AssistedFactory
    @CircuitInject(NewProjectQuestionScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: NewProjectQuestionScreen,
            navigator: Navigator,
        ): NewProjectQuestionPresenter
    }
}

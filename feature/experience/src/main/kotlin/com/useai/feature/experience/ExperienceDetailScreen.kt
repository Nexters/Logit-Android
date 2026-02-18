package com.useai.feature.experience

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.model.experience.Experience
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExperienceDetailScreen(
    val experienceId: String,
) : Screen {
    sealed interface State : CircuitUiState {
        data class Success(
            val experience: Experience,
            val eventSink: (Event) -> Unit,
        ) : State

        data class LoadFailed(
            val eventSink: (Event) -> Unit,
        ) : State

        data object Loading : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Retry : Event
        data object Back : Event
        data object ClickMore : Event
    }
}

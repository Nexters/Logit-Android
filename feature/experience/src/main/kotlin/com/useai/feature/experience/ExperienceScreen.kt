package com.useai.feature.experience

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.model.experience.Experience
import kotlinx.parcelize.Parcelize

@Parcelize
data object ExperienceScreen : Screen {
    sealed interface State : CircuitUiState {
        data class Success(
            val experiences: List<Experience>,
            val eventSink: (Event) -> Unit,
        ) : State

        data class LoadFailed(
            val eventSink: (Event) -> Unit,
        ) : State

        data object Loading : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Retry : Event
        data object ClickAddExperience : Event
        data object ClickRegisterExperience : Event
        data class ClickExperienceCard(val experienceId: String) : Event
        data class ClickExperienceMore(val experienceId: String) : Event
    }
}

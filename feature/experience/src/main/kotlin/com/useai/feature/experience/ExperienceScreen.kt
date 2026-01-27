package com.useai.feature.experience

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object ExperienceScreen : Screen {
    sealed interface State : CircuitUiState
    sealed interface Event : CircuitUiEvent
}

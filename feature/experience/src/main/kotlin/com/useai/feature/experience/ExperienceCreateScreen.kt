package com.useai.feature.experience

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.screen.PopResult
import kotlinx.parcelize.Parcelize

@Parcelize
data object ExperienceCreateScreen : Screen {
    @Parcelize
    data class ExperienceCreatedResult(
        val experienceId: String,
    ) : PopResult

    data class State(
        val currentStep: ExperienceCreateStep,
        val title: String,
        val startDate: String,
        val isInProgress: Boolean,
        val selectedExperienceType: String?,
        val situation: String,
        val task: String,
        val action: String,
        val result: String,
        val isSubmitting: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data object Next : Event
        data object LoadExample : Event
        data class ChangeTitle(val value: String) : Event
        data class ChangeStartDate(val value: String) : Event
        data object ToggleInProgress : Event
        data class SelectExperienceType(val value: String) : Event
        data class ChangeSituation(val value: String) : Event
        data class ChangeTask(val value: String) : Event
        data class ChangeAction(val value: String) : Event
        data class ChangeResult(val value: String) : Event
    }
}

enum class ExperienceCreateStep(
    val step: Int,
    val total: Int = 2,
) {
    BASIC_INFO(1),
    STAR(2)
}

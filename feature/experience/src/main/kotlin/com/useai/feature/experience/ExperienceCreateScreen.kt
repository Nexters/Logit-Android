package com.useai.feature.experience

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.screen.PopResult
import com.useai.core.model.experience.ExperienceType
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class ExperienceCreateScreen(
    val experienceId: String? = null,
) : Screen {
    @Parcelize
    data class ExperienceSubmitResult(
        val experienceId: String,
        val action: Action,
    ) : PopResult

    enum class Action {
        CREATED,
        UPDATED,
    }

    data class State(
        val currentStep: ExperienceCreateStep,
        val isEditMode: Boolean,
        val title: String,
        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val isInProgress: Boolean,
        val selectedExperienceType: ExperienceType?,
        val selectedFormatType: ExperienceCreateFormatType,
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
        data class ChangeStartDate(val value: LocalDate?) : Event
        data class ChangeEndDate(val value: LocalDate?) : Event
        data object ToggleInProgress : Event
        data class SelectExperienceType(val value: ExperienceType) : Event
        data class SelectFormatType(val value: ExperienceCreateFormatType) : Event
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

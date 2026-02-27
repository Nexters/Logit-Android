package com.useai.feature.experience

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.component.snackbar.showLogitSnackbar
import com.useai.core.model.experience.ExperienceParam
import com.useai.core.model.experience.ExperienceType
import com.useai.core.network.request.UpdateExperienceRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExperienceCreatePresenter @AssistedInject constructor(
    private val experienceRepository: ExperienceRepository,
    @Assisted private val screen: ExperienceCreateScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<ExperienceCreateScreen.State> {

    @Composable
    override fun present(): ExperienceCreateScreen.State {
        val scope = rememberStableCoroutineScope()
        val snackbarHostState = LocalLogitSnackbarHostState.current
        val invalidInputMessage = stringResource(R.string.experience_create_invalid_input)
        val createFailMessage = stringResource(R.string.experience_create_failed)

        var currentStep by rememberRetained { mutableStateOf(ExperienceCreateStep.BASIC_INFO) }
        var title by rememberRetained { mutableStateOf("") }
        var startDate by rememberRetained { mutableStateOf<LocalDate?>(null) }
        var endDate by rememberRetained { mutableStateOf<LocalDate?>(null) }
        var isInProgress by rememberRetained { mutableStateOf(false) }
        var selectedExperienceType by rememberRetained { mutableStateOf<ExperienceType?>(null) }
        var selectedFormatType by rememberRetained { mutableStateOf(ExperienceCreateFormatType.STAR) }
        var situation by rememberRetained { mutableStateOf("") }
        var task by rememberRetained { mutableStateOf("") }
        var action by rememberRetained { mutableStateOf("") }
        var result by rememberRetained { mutableStateOf("") }
        var formatDrafts by rememberRetained {
            mutableStateOf<Map<ExperienceCreateFormatType, InputDraft>>(emptyMap())
        }
        var isSubmitting by rememberRetained { mutableStateOf(false) }
        var isPrefilled by rememberRetained { mutableStateOf(false) }

        if (!isPrefilled && screen.experienceId != null) {
            isPrefilled = true
            scope.launch {
                experienceRepository.getExperience(screen.experienceId)
                    .onSuccess { experience ->
                        title = experience.title
                        startDate = experience.startDate
                        endDate = experience.endDate
                        isInProgress = experience.endDate == null
                        selectedExperienceType = experience.experienceType
                        selectedFormatType = experience.formatType.toFormatType()
                        situation = experience.situation
                        task = experience.task
                        action = experience.action
                        result = experience.result
                    }.onFailure {
                        snackbarHostState.showLogitSnackbar(
                            message = createFailMessage,
                            iconResId = R.drawable.ic_experience_default
                        )
                    }
            }
        }

        val eventSink: (ExperienceCreateScreen.Event) -> Unit = { event ->
            when (event) {
                ExperienceCreateScreen.Event.Back -> {
                    if (currentStep == ExperienceCreateStep.BASIC_INFO) {
                        navigator.pop()
                    } else {
                        currentStep = currentStep.previous()
                    }
                }

                ExperienceCreateScreen.Event.LoadExample -> {
                    when (currentStep) {
                        ExperienceCreateStep.BASIC_INFO -> {
                            title = ExperienceCreateDefaults.sampleTitle
                            startDate = LocalDate.of(2022, 4, 6)
                            isInProgress = true
                            selectedExperienceType = ExperienceType.entries.first()
                        }

                        ExperienceCreateStep.STAR -> {
                            when (selectedFormatType) {
                                ExperienceCreateFormatType.FREEFORM -> {
                                    situation = ExperienceCreateDefaults.sampleFreeform
                                    task = ""
                                    action = ""
                                    result = ""
                                }

                                ExperienceCreateFormatType.PSI -> {
                                    situation = ExperienceCreateDefaults.sampleProblem
                                    task = ExperienceCreateDefaults.sampleSolution
                                    action = ExperienceCreateDefaults.sampleInsight
                                    result = ""
                                }

                                ExperienceCreateFormatType.STAR -> {
                                    situation = ExperienceCreateDefaults.sampleSituation
                                    task = ExperienceCreateDefaults.sampleTask
                                    action = ExperienceCreateDefaults.sampleAction
                                    result = ExperienceCreateDefaults.sampleResult
                                }
                            }
                        }
                    }
                }

                ExperienceCreateScreen.Event.Next -> {
                    when (currentStep) {
                        ExperienceCreateStep.BASIC_INFO -> {
                            if (isBasicInfoValid(title, startDate, selectedExperienceType)) {
                                currentStep = ExperienceCreateStep.STAR
                            } else {
                                scope.launch {
                                    snackbarHostState.showLogitSnackbar(
                                        message = invalidInputMessage,
                                        iconResId = R.drawable.ic_experience_default
                                    )
                                }
                            }
                        }

                        ExperienceCreateStep.STAR -> {
                            if (isSubmitting) {
                                scope.launch {
                                    snackbarHostState.showLogitSnackbar(
                                        message = invalidInputMessage,
                                        iconResId = R.drawable.ic_experience_default
                                    )
                                }
                            } else {
                                val startDateSnapshot = startDate
                                val endDateSnapshot = if (isInProgress) null else endDate

                                if (
                                    !isStep2Valid(selectedFormatType, situation, task, action, result) ||
                                    startDateSnapshot == null ||
                                    selectedExperienceType == null ||
                                    (!isInProgress && endDateSnapshot == null)
                                ) {
                                    scope.launch {
                                        snackbarHostState.showLogitSnackbar(
                                            message = invalidInputMessage,
                                            iconResId = R.drawable.ic_experience_default
                                        )
                                    }
                                } else {
                                    isSubmitting = true
                                    val formattedInput = toFormattedInput(
                                        formatType = selectedFormatType,
                                        situation = situation,
                                        task = task,
                                        action = action,
                                        result = result
                                    )
                                    scope.launch {
                                        val experienceId = screen.experienceId
                                        if (experienceId == null) {
                                            val title = title.trim()
                                            val experienceType = selectedExperienceType?.typeName ?: ""
                                            val formatType = selectedFormatType.requestValue
                                            val experienceParam = when (selectedFormatType) {
                                                ExperienceCreateFormatType.STAR -> {
                                                    ExperienceParam(
                                                        title = title,
                                                        experienceType = experienceType,
                                                        formatType = formatType,
                                                        startDate = startDateSnapshot,
                                                        endDate = endDateSnapshot,
                                                        tags = "",
                                                        situation = formattedInput.situation,
                                                        task = formattedInput.task,
                                                        action = formattedInput.action,
                                                        result = formattedInput.result,
                                                    )
                                                }

                                                ExperienceCreateFormatType.PSI -> {
                                                    ExperienceParam(
                                                        title = title,
                                                        experienceType = experienceType,
                                                        formatType = formatType,
                                                        startDate = startDateSnapshot,
                                                        endDate = endDateSnapshot,
                                                        tags = "",
                                                        problem = formattedInput.situation,
                                                        solution = formattedInput.task,
                                                        insight = formattedInput.action,
                                                    )
                                                }

                                                ExperienceCreateFormatType.FREEFORM -> {
                                                    ExperienceParam(
                                                        title = title,
                                                        experienceType = experienceType,
                                                        formatType = formatType,
                                                        startDate = startDateSnapshot,
                                                        endDate = endDateSnapshot,
                                                        tags = "",
                                                        content = formattedInput.situation,
                                                    )
                                                }
                                            }

                                            experienceRepository.createExperience(
                                                experience = experienceParam,
                                            ).onSuccess { created ->
                                                isSubmitting = false
                                                navigator.pop(
                                                    ExperienceCreateScreen.ExperienceSubmitResult(
                                                        experienceId = created.id,
                                                        action = ExperienceCreateScreen.Action.CREATED
                                                    )
                                                )
                                            }.onFailure {
                                                isSubmitting = false
                                                snackbarHostState.showLogitSnackbar(
                                                    message = createFailMessage,
                                                    iconResId = R.drawable.ic_experience_default
                                                )
                                            }
                                        } else {
                                            experienceRepository.updateExperience(
                                                experienceId = experienceId,
                                                request = UpdateExperienceRequest(
                                                    title = title.trim(),
                                                    experienceType = selectedExperienceType?.typeName ?: "",
                                                    formatType = selectedFormatType.requestValue,
                                                    startDate = startDateSnapshot.toString(),
                                                    endDate = endDateSnapshot?.toString(),
                                                    tags = "",
                                                    situation = formattedInput.situation,
                                                    task = formattedInput.task,
                                                    action = formattedInput.action,
                                                    result = formattedInput.result,
                                                    problem = if (selectedFormatType == ExperienceCreateFormatType.PSI) {
                                                        formattedInput.situation
                                                    } else {
                                                        null
                                                    },
                                                    solution = if (selectedFormatType == ExperienceCreateFormatType.PSI) {
                                                        formattedInput.task
                                                    } else {
                                                        null
                                                    },
                                                    insight = if (selectedFormatType == ExperienceCreateFormatType.PSI) {
                                                        formattedInput.action
                                                    } else {
                                                        null
                                                    },
                                                    content = if (selectedFormatType == ExperienceCreateFormatType.FREEFORM) {
                                                        formattedInput.situation
                                                    } else {
                                                        null
                                                    }
                                                )
                                            )
                                                .onSuccess { updated ->
                                                    isSubmitting = false
                                                    navigator.pop(
                                                        ExperienceCreateScreen.ExperienceSubmitResult(
                                                            experienceId = updated.id,
                                                            action = ExperienceCreateScreen.Action.UPDATED
                                                        )
                                                    )
                                                }.onFailure {
                                                    isSubmitting = false
                                                    snackbarHostState.showLogitSnackbar(
                                                        message = createFailMessage,
                                                        iconResId = R.drawable.ic_experience_default
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is ExperienceCreateScreen.Event.ChangeTitle -> title = event.value
                is ExperienceCreateScreen.Event.ChangeStartDate -> startDate = event.value
                is ExperienceCreateScreen.Event.ChangeEndDate -> endDate = event.value
                ExperienceCreateScreen.Event.ToggleInProgress -> isInProgress = !isInProgress

                is ExperienceCreateScreen.Event.SelectExperienceType -> selectedExperienceType = event.value
                is ExperienceCreateScreen.Event.SelectFormatType -> {
                    if (selectedFormatType != event.value) {
                        val updatedDrafts = formatDrafts + (
                            selectedFormatType to InputDraft(
                                situation = situation,
                                task = task,
                                action = action,
                                result = result
                            )
                        )
                        formatDrafts = updatedDrafts
                        selectedFormatType = event.value
                        val selectedDraft = updatedDrafts[event.value] ?: InputDraft.EMPTY
                        situation = selectedDraft.situation
                        task = selectedDraft.task
                        action = selectedDraft.action
                        result = selectedDraft.result
                    }
                }
                is ExperienceCreateScreen.Event.ChangeSituation -> {
                    situation = event.value
                    formatDrafts = formatDrafts + (
                        selectedFormatType to InputDraft(
                            situation = situation,
                            task = task,
                            action = action,
                            result = result
                        )
                    )
                }
                is ExperienceCreateScreen.Event.ChangeTask -> {
                    task = event.value
                    formatDrafts = formatDrafts + (
                        selectedFormatType to InputDraft(
                            situation = situation,
                            task = task,
                            action = action,
                            result = result
                        )
                    )
                }
                is ExperienceCreateScreen.Event.ChangeAction -> {
                    action = event.value
                    formatDrafts = formatDrafts + (
                        selectedFormatType to InputDraft(
                            situation = situation,
                            task = task,
                            action = action,
                            result = result
                        )
                    )
                }
                is ExperienceCreateScreen.Event.ChangeResult -> {
                    result = event.value
                    formatDrafts = formatDrafts + (
                        selectedFormatType to InputDraft(
                            situation = situation,
                            task = task,
                            action = action,
                            result = result
                        )
                    )
                }
            }
        }

        return ExperienceCreateScreen.State(
            currentStep = currentStep,
            isEditMode = screen.experienceId != null,
            title = title,
            startDate = startDate,
            endDate = endDate,
            isInProgress = isInProgress,
            selectedExperienceType = selectedExperienceType,
            selectedFormatType = selectedFormatType,
            situation = situation,
            task = task,
            action = action,
            result = result,
            isSubmitting = isSubmitting,
            eventSink = eventSink
        )
    }

    @AssistedFactory
    @CircuitInject(ExperienceCreateScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: ExperienceCreateScreen,
            navigator: Navigator,
        ): ExperienceCreatePresenter
    }

    private fun String?.toFormatType(): ExperienceCreateFormatType = when (this?.trim()?.uppercase()) {
        ExperienceCreateFormatType.PSI.requestValue -> ExperienceCreateFormatType.PSI
        ExperienceCreateFormatType.FREEFORM.requestValue -> ExperienceCreateFormatType.FREEFORM
        else -> ExperienceCreateFormatType.STAR
    }

    private fun isBasicInfoValid(
        title: String,
        startDate: LocalDate?,
        selectedExperienceType: ExperienceType?,
    ): Boolean {
        return title.isNotBlank() && startDate != null && selectedExperienceType != null
    }

    private fun isStep2Valid(
        formatType: ExperienceCreateFormatType,
        situation: String,
        task: String,
        action: String,
        result: String,
    ): Boolean {
        return when (formatType) {
            ExperienceCreateFormatType.STAR ->
                situation.trim().length >= MIN_INPUT_LENGTH &&
                    task.trim().length >= MIN_INPUT_LENGTH &&
                    action.trim().length >= MIN_INPUT_LENGTH &&
                    result.trim().length >= MIN_INPUT_LENGTH

            ExperienceCreateFormatType.PSI ->
                situation.trim().length >= MIN_INPUT_LENGTH &&
                    task.trim().length >= MIN_INPUT_LENGTH &&
                    action.trim().length >= MIN_INPUT_LENGTH

            ExperienceCreateFormatType.FREEFORM ->
                situation.trim().length >= MIN_INPUT_LENGTH
        }
    }

    private fun toFormattedInput(
        formatType: ExperienceCreateFormatType,
        situation: String,
        task: String,
        action: String,
        result: String,
    ): FormattedInput {
        val trimmedSituation = situation.trim()
        val trimmedTask = task.trim()
        val trimmedAction = action.trim()
        val trimmedResult = result.trim()
        return when (formatType) {
            ExperienceCreateFormatType.STAR -> FormattedInput(
                situation = trimmedSituation,
                task = trimmedTask,
                action = trimmedAction,
                result = trimmedResult
            )

            ExperienceCreateFormatType.PSI -> FormattedInput(
                situation = trimmedSituation,
                task = trimmedTask,
                action = trimmedAction,
                result = ""
            )

            ExperienceCreateFormatType.FREEFORM -> FormattedInput(
                situation = trimmedSituation,
                task = "",
                action = "",
                result = ""
            )
        }
    }

    private fun ExperienceCreateStep.previous(): ExperienceCreateStep = when (this) {
        ExperienceCreateStep.BASIC_INFO -> ExperienceCreateStep.BASIC_INFO
        ExperienceCreateStep.STAR -> ExperienceCreateStep.BASIC_INFO
    }

    private data class FormattedInput(
        val situation: String,
        val task: String,
        val action: String,
        val result: String,
    )

    private data class InputDraft(
        val situation: String,
        val task: String,
        val action: String,
        val result: String,
    ) {
        companion object {
            val EMPTY = InputDraft(
                situation = "",
                task = "",
                action = "",
                result = ""
            )
        }
    }

    companion object {
        private const val MIN_INPUT_LENGTH = 50
    }
}

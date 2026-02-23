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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class ExperienceCreatePresenter @AssistedInject constructor(
    private val experienceRepository: ExperienceRepository,
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
        var startDate by rememberRetained { mutableStateOf("") }
        var isInProgress by rememberRetained { mutableStateOf(false) }
        var selectedExperienceType by rememberRetained { mutableStateOf<String?>(null) }
        var situation by rememberRetained { mutableStateOf("") }
        var task by rememberRetained { mutableStateOf("") }
        var action by rememberRetained { mutableStateOf("") }
        var result by rememberRetained { mutableStateOf("") }
        var isSubmitting by rememberRetained { mutableStateOf(false) }

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
                            startDate = "2022. 04. 06"
                            isInProgress = true
                            selectedExperienceType = ExperienceCreateDefaults.experienceTypes.first()
                        }

                        ExperienceCreateStep.STAR -> {
                            situation = ExperienceCreateDefaults.sampleSituation
                            task = ExperienceCreateDefaults.sampleTask
                            action = ExperienceCreateDefaults.sampleAction
                            result = ExperienceCreateDefaults.sampleResult
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
                                val parsedStartDate = parseDate(startDate)
                                val parsedEndDate = if (isInProgress) null else parsedStartDate
                                if (
                                    !isStarValid(situation, task, action, result) ||
                                    parsedStartDate == null ||
                                    selectedExperienceType == null ||
                                    (!isInProgress && parsedEndDate == null)
                                ) {
                                    scope.launch {
                                        snackbarHostState.showLogitSnackbar(
                                            message = invalidInputMessage,
                                            iconResId = R.drawable.ic_experience_default
                                        )
                                    }
                                } else {
                                    isSubmitting = true
                                    scope.launch {
                                        experienceRepository.createExperience(
                                            ExperienceParam(
                                                title = title.trim(),
                                                experienceType = selectedExperienceType.orEmpty(),
                                                formatType = FORMAT_TYPE_STAR,
                                                startDate = parsedStartDate,
                                                endDate = parsedEndDate,
                                                tags = "",
                                                situation = situation.trim(),
                                                task = task.trim(),
                                                action = action.trim(),
                                                result = result.trim()
                                            )
                                        ).onSuccess { created ->
                                            isSubmitting = false
                                            navigator.pop(
                                                ExperienceCreateScreen.ExperienceCreatedResult(
                                                    experienceId = created.id
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

                is ExperienceCreateScreen.Event.ChangeTitle -> title = event.value
                is ExperienceCreateScreen.Event.ChangeStartDate -> startDate = event.value
                ExperienceCreateScreen.Event.ToggleInProgress -> isInProgress = !isInProgress

                is ExperienceCreateScreen.Event.SelectExperienceType -> selectedExperienceType = event.value
                is ExperienceCreateScreen.Event.ChangeSituation -> situation = event.value
                is ExperienceCreateScreen.Event.ChangeTask -> task = event.value
                is ExperienceCreateScreen.Event.ChangeAction -> action = event.value
                is ExperienceCreateScreen.Event.ChangeResult -> result = event.value
            }
        }

        return ExperienceCreateScreen.State(
            currentStep = currentStep,
            title = title,
            startDate = startDate,
            isInProgress = isInProgress,
            selectedExperienceType = selectedExperienceType,
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
            navigator: Navigator,
        ): ExperienceCreatePresenter
    }

    private fun isBasicInfoValid(
        title: String,
        startDate: String,
        selectedExperienceType: String?,
    ): Boolean {
        return title.isNotBlank() && parseDate(startDate) != null && selectedExperienceType != null
    }

    private fun isStarValid(
        situation: String,
        task: String,
        action: String,
        result: String,
    ): Boolean {
        return situation.trim().length >= MIN_STAR_LENGTH &&
            task.trim().length >= MIN_STAR_LENGTH &&
            action.trim().length >= MIN_STAR_LENGTH &&
            result.trim().length >= MIN_STAR_LENGTH
    }

    private fun parseDate(raw: String): LocalDate? {
        return try {
            LocalDate.parse(raw.trim(), DATE_INPUT_FORMATTER)
        } catch (_: DateTimeParseException) {
            null
        }
    }

    private fun ExperienceCreateStep.previous(): ExperienceCreateStep = when (this) {
        ExperienceCreateStep.BASIC_INFO -> ExperienceCreateStep.BASIC_INFO
        ExperienceCreateStep.STAR -> ExperienceCreateStep.BASIC_INFO
    }

    companion object {
        private const val MIN_STAR_LENGTH = 50
        private const val FORMAT_TYPE_STAR = "STAR"
        private val DATE_INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy. MM. dd", Locale.KOREA)
    }
}

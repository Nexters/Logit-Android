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
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.experience.ExperienceParam
import com.useai.core.ui.fullName
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
        var endDate by rememberRetained { mutableStateOf("") }
        var isInProgress by rememberRetained { mutableStateOf(false) }
        var selectedExperienceType by rememberRetained { mutableStateOf<String?>(null) }
        var situation by rememberRetained { mutableStateOf("") }
        var task by rememberRetained { mutableStateOf("") }
        var action by rememberRetained { mutableStateOf("") }
        var result by rememberRetained { mutableStateOf("") }
        var selectedCategory by rememberRetained { mutableStateOf<ExperienceCategory?>(null) }
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
                            endDate = "2022. 04. 06"
                            isInProgress = false
                            selectedExperienceType = ExperienceCreateDefaults.experienceTypes.first()
                        }

                        ExperienceCreateStep.STAR -> {
                            situation = ExperienceCreateDefaults.sampleSituation
                            task = ExperienceCreateDefaults.sampleTask
                            action = ExperienceCreateDefaults.sampleAction
                            result = ExperienceCreateDefaults.sampleResult
                        }

                        ExperienceCreateStep.CATEGORY -> {
                            selectedCategory = ExperienceCategory.CUSTOMER_VALUE_ORIENTATION
                        }
                    }
                }

                ExperienceCreateScreen.Event.Next -> {
                    when (currentStep) {
                        ExperienceCreateStep.BASIC_INFO -> {
                            if (isBasicInfoValid(title, startDate, endDate, isInProgress, selectedExperienceType)) {
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
                            if (isStarValid(situation, task, action, result)) {
                                currentStep = ExperienceCreateStep.CATEGORY
                            } else {
                                scope.launch {
                                    snackbarHostState.showLogitSnackbar(
                                        message = invalidInputMessage,
                                        iconResId = R.drawable.ic_experience_default
                                    )
                                }
                            }
                        }

                        ExperienceCreateStep.CATEGORY -> {
                            if (isSubmitting || selectedCategory == null) {
                                scope.launch {
                                    snackbarHostState.showLogitSnackbar(
                                        message = invalidInputMessage,
                                        iconResId = R.drawable.ic_experience_default
                                    )
                                }
                            } else {
                                val selected = requireNotNull(selectedCategory)
                                val parsedStartDate = parseDate(startDate)
                                if (parsedStartDate == null || selectedExperienceType == null) {
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
                                            situation = situation.trim(),
                                            task = task.trim(),
                                            action = action.trim(),
                                            result = result.trim(),
                                            category = selected.fullName,
                                            date = parsedStartDate,
                                            experienceType = selectedExperienceType.orEmpty(),
                                            title = title.trim()
                                        )
                                        ).onSuccess { created ->
                                            isSubmitting = false
                                            navigator.goTo(ExperienceDetailScreen(created.id))
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
                is ExperienceCreateScreen.Event.ChangeEndDate -> endDate = event.value
                ExperienceCreateScreen.Event.ToggleInProgress -> {
                    isInProgress = !isInProgress
                    if (isInProgress) endDate = ""
                }

                is ExperienceCreateScreen.Event.SelectExperienceType -> selectedExperienceType = event.value
                is ExperienceCreateScreen.Event.ChangeSituation -> situation = event.value
                is ExperienceCreateScreen.Event.ChangeTask -> task = event.value
                is ExperienceCreateScreen.Event.ChangeAction -> action = event.value
                is ExperienceCreateScreen.Event.ChangeResult -> result = event.value
                is ExperienceCreateScreen.Event.SelectCategory -> selectedCategory = event.value
            }
        }

        return ExperienceCreateScreen.State(
            currentStep = currentStep,
            title = title,
            startDate = startDate,
            endDate = endDate,
            isInProgress = isInProgress,
            selectedExperienceType = selectedExperienceType,
            situation = situation,
            task = task,
            action = action,
            result = result,
            selectedCategory = selectedCategory,
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
        endDate: String,
        isInProgress: Boolean,
        selectedExperienceType: String?,
    ): Boolean {
        val hasValidDate = parseDate(startDate) != null &&
            (isInProgress || parseDate(endDate) != null)
        return title.isNotBlank() && hasValidDate && selectedExperienceType != null
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
        ExperienceCreateStep.CATEGORY -> ExperienceCreateStep.STAR
    }

    companion object {
        private const val MIN_STAR_LENGTH = 50
        private val DATE_INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy. MM. dd", Locale.KOREA)
    }
}

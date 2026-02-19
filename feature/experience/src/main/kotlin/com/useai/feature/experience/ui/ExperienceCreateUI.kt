package com.useai.feature.experience.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFormContainer
import com.useai.core.ui.LogitStepper
import com.useai.feature.experience.ExperienceCreateScreen
import com.useai.feature.experience.ExperienceCreateStep
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(ExperienceCreateScreen::class, ActivityRetainedComponent::class)
fun ExperienceCreateUI(
    state: ExperienceCreateScreen.State,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        state.eventSink(ExperienceCreateScreen.Event.Back)
    }

    InputFormContainer(
        modifier = modifier.statusBarsPadding(),
        onClickBackButton = {
            state.eventSink(ExperienceCreateScreen.Event.Back)
        },
        bottomButtonText = if (state.currentStep == ExperienceCreateStep.CATEGORY) {
            stringResource(R.string.experience_register)
        } else {
            stringResource(R.string.next)
        },
        onClickBottomButton = {
            state.eventSink(ExperienceCreateScreen.Event.Next)
        },
        bottomButtonEnabled = isBottomButtonEnabled(state),
    ) {
        LogitStepper(
            currentStep = state.currentStep.step.toString(),
            totalStep = state.currentStep.total.toString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (state.currentStep) {
            ExperienceCreateStep.BASIC_INFO -> ExperienceCreateBasicInfoStep(state = state)
            ExperienceCreateStep.STAR -> ExperienceCreateStarStep(state = state)
            ExperienceCreateStep.CATEGORY -> ExperienceCreateCategoryStep(state = state)
        }
    }
}

private fun isBottomButtonEnabled(state: ExperienceCreateScreen.State): Boolean {
    return when (state.currentStep) {
        ExperienceCreateStep.BASIC_INFO -> {
            state.title.isNotBlank() &&
                isDateFormatValid(state.startDate) &&
                (state.isInProgress || isDateFormatValid(state.endDate)) &&
                state.selectedExperienceType != null
        }

        ExperienceCreateStep.STAR -> {
            state.situation.trim().length >= 50 &&
                state.task.trim().length >= 50 &&
                state.action.trim().length >= 50 &&
                state.result.trim().length >= 50
        }

        ExperienceCreateStep.CATEGORY -> {
            state.selectedCategory != null && !state.isSubmitting
        }
    }
}

private val DATE_INPUT_REGEX by lazy {
    Regex("^\\d{4}\\.\\s\\d{2}\\.\\s\\d{2}$")
}

private fun isDateFormatValid(value: String): Boolean {
    return DATE_INPUT_REGEX.matches(value.trim())
}

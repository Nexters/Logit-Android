package com.useai.feature.experience.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.ui.InputFormContainer
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
        bottomButtonText = stringResource(
            if (state.currentStep == ExperienceCreateStep.STAR) {
                R.string.experience_register
            } else {
                R.string.next
            }
        ),
        onClickBottomButton = {
            state.eventSink(ExperienceCreateScreen.Event.Next)
        },
        bottomButtonEnabled = isBottomButtonEnabled(state),
    ) {
        when (state.currentStep) {
            ExperienceCreateStep.BASIC_INFO -> ExperienceCreateBasicInfoStep(state = state)
            ExperienceCreateStep.STAR -> ExperienceCreateStarStep(state = state)
        }
    }
}

private fun isBottomButtonEnabled(state: ExperienceCreateScreen.State): Boolean {
    return when (state.currentStep) {
        ExperienceCreateStep.BASIC_INFO -> {
            state.title.isNotBlank() &&
                isDateFormatValid(state.startDate) &&
                state.selectedExperienceType != null
        }

        ExperienceCreateStep.STAR -> {
            state.situation.trim().length >= 50 &&
                state.task.trim().length >= 50 &&
                state.action.trim().length >= 50 &&
                state.result.trim().length >= 50
        }
    }
}

private val DATE_INPUT_REGEX by lazy {
    Regex("^\\d{4}\\.\\s\\d{2}\\.\\s\\d{2}$")
}

private fun isDateFormatValid(value: String): Boolean {
    return DATE_INPUT_REGEX.matches(value.trim())
}

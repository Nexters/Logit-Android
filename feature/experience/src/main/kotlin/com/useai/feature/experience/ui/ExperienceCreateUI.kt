package com.useai.feature.experience.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFormContainer
import com.useai.core.model.experience.ExperienceCreateFormatType
import com.useai.feature.experience.ExperienceCreateScreen
import com.useai.feature.experience.ExperienceCreateStep
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(ExperienceCreateScreen::class, ActivityRetainedComponent::class)
fun ExperienceCreateUI(
    state: ExperienceCreateScreen.State,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    BackHandler {
        state.eventSink(ExperienceCreateScreen.Event.Back)
    }

    InputFormContainer(
        modifier = modifier
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        onClickBackButton = {
            state.eventSink(ExperienceCreateScreen.Event.Back)
        },
        bottomButtonText = stringResource(
            if (state.currentStep == ExperienceCreateStep.STAR) {
                if (state.isEditMode) R.string.chat_edit else R.string.experience_register
            } else {
                R.string.next
            }
        ),
        onClickBottomButton = {
            state.eventSink(ExperienceCreateScreen.Event.Next)
        },
        bottomButtonEnabled = isBottomButtonEnabled(state),
        contentScrollEnabled = false,
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
                state.startDate != null &&
                state.selectedExperienceType != null
        }

        ExperienceCreateStep.STAR -> {
            when (state.selectedFormatType) {
                ExperienceCreateFormatType.STAR ->
                    state.situation.trim().length >= 50 &&
                        state.task.trim().length >= 50 &&
                        state.action.trim().length >= 50 &&
                        state.result.trim().length >= 50

                ExperienceCreateFormatType.PSI ->
                    state.situation.trim().length >= 50 &&
                        state.task.trim().length >= 50 &&
                        state.action.trim().length >= 50

                ExperienceCreateFormatType.FREEFORM ->
                    state.situation.trim().length >= 50
            }
        }
    }
}

@Preview
@Composable
private fun ExperienceCreateUIPreview() {
    LogitTheme {
        ExperienceCreateUI(
            state = ExperienceCreateScreen.State(
                currentStep = ExperienceCreateStep.BASIC_INFO,
                isEditMode = false,
                title = "",
                startDate = null,
                endDate = null,
                isInProgress = false,
                selectedExperienceType = null,
                selectedFormatType = ExperienceCreateFormatType.STAR,
                situation = "",
                task = "",
                action = "",
                result = "",
                isSubmitting = false,
                eventSink = {}
            )
        )
    }
}

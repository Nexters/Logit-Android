package com.useai.feature.experience.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.ExperienceType
import com.useai.core.ui.InputFieldLabel
import com.useai.core.ui.LogitDateInputField
import com.useai.core.ui.LogitInputField
import com.useai.core.ui.LogitStepper
import com.useai.core.ui.ProgressCheckBox
import com.useai.feature.experience.ExperienceCreateFormatType
import com.useai.feature.experience.ExperienceCreateScreen
import com.useai.feature.experience.ExperienceCreateStep

@Composable
internal fun ExperienceCreateBasicInfoStep(
    modifier: Modifier = Modifier,
    state: ExperienceCreateScreen.State,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LogitTheme.colors.white)
            ) {
                LogitStepper(
                    currentStep = ExperienceCreateStep.BASIC_INFO.step.toString(),
                    totalStep = ExperienceCreateStep.BASIC_INFO.total.toString(),
                )

                Spacer(modifier = Modifier.padding(top = 13.dp))

                ExperienceCreateSectionHeader(
                    title = stringResource(R.string.experience_create_step1_title),
                    description = stringResource(R.string.experience_create_step1_desc),
                    onClickLoadExample = { state.eventSink(ExperienceCreateScreen.Event.LoadExample) }
                )

                Spacer(modifier = Modifier.padding(top = 20.dp))
            }
        }

        item {
            LogitInputField(
                label = stringResource(R.string.experience_create_title_label),
                isRequired = true,
                maxLength = 100,
                input = state.title,
                onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeTitle(it)) },
                placeHolder = stringResource(R.string.experience_create_title_placeholder),
                focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200)
            )

            Spacer(modifier = Modifier.padding(top = 36.dp))
            InputFieldLabel(
                text = stringResource(R.string.experience_create_date_label),
                isRequired = true,
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LogitDateInputField(
                    modifier = Modifier.weight(1f),
                    dueDate = state.startDate,
                    enabled = true,
                    onDateSelected = {
                        state.eventSink(ExperienceCreateScreen.Event.ChangeStartDate(it))
                    }
                )
                Text(
                    text = "~",
                    modifier = Modifier.width(28.dp),
                    color = LogitTheme.colors.gray200,
                    textAlign = TextAlign.Center,
                )
                LogitDateInputField(
                    modifier = Modifier.weight(1f),
                    dueDate = state.endDate,
                    enabled = state.isInProgress.not(),
                    onDateSelected = {
                        state.eventSink(ExperienceCreateScreen.Event.ChangeEndDate(it))
                    }
                )
            }

            ProgressCheckBox(
                checked = state.isInProgress,
                text = "진행 중",
                onCheckedChange = { state.eventSink(ExperienceCreateScreen.Event.ToggleInProgress) },
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.padding(top = 36.dp))
            InputFieldLabel(
                text = stringResource(R.string.experience_create_type_label),
                isRequired = true
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExperienceType.entries.fastForEach { type ->
                    ExperienceSelectableChip(
                        text = type.displayName,
                        selected = state.selectedExperienceType == type,
                        onClick = { state.eventSink(ExperienceCreateScreen.Event.SelectExperienceType(type)) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExperienceCreateBasicInfoStepPreview() {
    LogitTheme {
        ExperienceCreateBasicInfoStep(
            modifier = Modifier.background(LogitTheme.colors.white),
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

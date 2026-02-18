package com.useai.feature.experience.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFieldLabel
import com.useai.core.ui.LogitInputField
import com.useai.feature.experience.ExperienceCreateDefaults
import com.useai.feature.experience.ExperienceCreateScreen

@Composable
internal fun ExperienceCreateBasicInfoStep(
    state: ExperienceCreateScreen.State,
) {
    Column {
        ExperienceCreateSectionHeader(
            title = stringResource(R.string.experience_create_step1_title),
            description = stringResource(R.string.experience_create_step1_desc),
            onClickLoadExample = { state.eventSink(ExperienceCreateScreen.Event.LoadExample) }
        )

        Spacer(modifier = Modifier.padding(top = 51.dp))

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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExperienceDateInputField(
                value = state.startDate,
                onValueChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeStartDate(it)) },
                placeholder = "YYYY. MM. DD",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "~",
                style = LogitTheme.typography.body5_4,
                color = LogitTheme.colors.gray200,
            )
            ExperienceDateInputField(
                value = state.endDate,
                onValueChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeEndDate(it)) },
                placeholder = "YYYY. MM. DD",
                modifier = Modifier.weight(1f),
                enabled = !state.isInProgress
            )
        }

        ProgressCheckBox(
            checked = state.isInProgress,
            onClick = { state.eventSink(ExperienceCreateScreen.Event.ToggleInProgress) },
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
            ExperienceCreateDefaults.experienceTypes.fastForEach { type ->
                ExperienceSelectableChip(
                    text = type,
                    selected = state.selectedExperienceType == type,
                    onClick = { state.eventSink(ExperienceCreateScreen.Event.SelectExperienceType(type)) }
                )
            }
        }
    }
}

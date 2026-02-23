package com.useai.feature.experience.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.LogitInputField
import com.useai.core.ui.LogitStepper
import com.useai.feature.experience.ExperienceCreateScreen
import com.useai.feature.experience.ExperienceCreateStep

@Composable
internal fun ExperienceCreateStarStep(
    state: ExperienceCreateScreen.State,
) {
    Column {
        LogitStepper(
            currentStep = ExperienceCreateStep.STAR.step.toString(),
            totalStep = ExperienceCreateStep.STAR.total.toString(),
        )

        Spacer(modifier = Modifier.padding(top = 13.dp))

        ExperienceCreateSectionHeader(
            title = "경험 정리",
            description = stringResource(R.string.experience_create_step2_desc),
            onClickLoadExample = { state.eventSink(ExperienceCreateScreen.Event.LoadExample) }
        )

        Spacer(modifier = Modifier.padding(top = 36.dp))

        ExperienceFormatTypeField(
            formatType = "STAR",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_situation),
            isRequired = true,
            maxLength = 1000,
            input = state.situation,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeSituation(it)) },
            placeHolder = stringResource(R.string.experience_create_situation_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200),
            minLines = 5
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_task),
            isRequired = true,
            maxLength = 1000,
            input = state.task,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeTask(it)) },
            placeHolder = stringResource(R.string.experience_create_task_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200),
            minLines = 4
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_action),
            isRequired = true,
            maxLength = 1000,
            input = state.action,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeAction(it)) },
            placeHolder = stringResource(R.string.experience_create_action_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200),
            minLines = 7
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_result),
            isRequired = true,
            maxLength = 1000,
            input = state.result,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeResult(it)) },
            placeHolder = stringResource(R.string.experience_create_result_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200),
            minLines = 6
        )
    }
}

@Composable
private fun ExperienceFormatTypeField(
    formatType: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "경험 정리방법",
            style = LogitTheme.typography.body5_3,
            color = LogitTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = LogitTheme.colors.gray100,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatType,
                style = LogitTheme.typography.body6_1,
                color = LogitTheme.colors.gray400
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = LogitTheme.colors.gray200
            )
        }
    }
}

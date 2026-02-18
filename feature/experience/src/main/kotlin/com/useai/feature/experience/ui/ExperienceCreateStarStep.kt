package com.useai.feature.experience.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.LogitInputField
import com.useai.feature.experience.ExperienceCreateScreen

@Composable
internal fun ExperienceCreateStarStep(
    state: ExperienceCreateScreen.State,
) {
    Column {
        ExperienceCreateSectionHeader(
            title = stringResource(R.string.experience_create_step2_title),
            description = stringResource(R.string.experience_create_step2_desc),
            onClickLoadExample = { state.eventSink(ExperienceCreateScreen.Event.LoadExample) }
        )

        Spacer(modifier = Modifier.padding(top = 51.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_situation),
            isRequired = true,
            maxLength = 1000,
            input = state.situation,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeSituation(it)) },
            placeHolder = stringResource(R.string.experience_create_situation_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200)
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_task),
            isRequired = true,
            maxLength = 1000,
            input = state.task,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeTask(it)) },
            placeHolder = stringResource(R.string.experience_create_task_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200)
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_action),
            isRequired = true,
            maxLength = 1000,
            input = state.action,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeAction(it)) },
            placeHolder = stringResource(R.string.experience_create_action_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200)
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))

        LogitInputField(
            label = stringResource(R.string.experience_detail_result),
            isRequired = true,
            maxLength = 1000,
            input = state.result,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeResult(it)) },
            placeHolder = stringResource(R.string.experience_create_result_placeholder),
            focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200)
        )
    }
}

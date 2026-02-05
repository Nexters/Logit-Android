package com.useai.feature.newproject.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFormContainer
import com.useai.core.ui.LogitFormTitle
import com.useai.core.ui.LogitInputField
import com.useai.core.ui.LogitStepper
import com.useai.feature.newproject.NewProjectBasicInfoScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@CircuitInject(NewProjectBasicInfoScreen::class, ActivityRetainedComponent::class)
fun NewProjectBasicInfo(
    state: NewProjectBasicInfoScreen.State,
    modifier: Modifier = Modifier
) {
    val isButtonEnabled = state.companyName.isNotBlank() &&
        state.jobName.isNotBlank() &&
        state.jobDesc.isNotBlank()

    InputFormContainer(
        modifier = modifier,
        onClickBackButton = {
            state.eventSink(NewProjectBasicInfoScreen.Event.Back)
        },
        bottomButtonText = stringResource(R.string.next),
        onClickBottomButton = {
            state.eventSink(NewProjectBasicInfoScreen.Event.Next)
        },
        bottomButtonEnabled = isButtonEnabled,
    ) {
        LogitStepper(
            currentStep = "1",
            totalStep = "2"
        )
        Spacer(
            modifier = Modifier.height(13.dp)
        )
        LogitFormTitle(
            title = stringResource(R.string.project_form_title_1),
            desc = stringResource(R.string.project_form_desc_1),
        )
        Spacer(
            modifier = Modifier.height(41.dp)
        )
        LogitInputField(
            label = stringResource(R.string.project_field_company_name_label),
            isRequired = true,
            maxLength = 100,
            input = state.companyName,
            onInputChange = { state.eventSink(NewProjectBasicInfoScreen.Event.OnCompanyNameChange(it)) },
            placeHolder = stringResource(R.string.project_field_company_name_placeholder),
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_between_fields))
        )
        LogitInputField(
            label = stringResource(R.string.project_field_job_name_label),
            isRequired = true,
            maxLength = 100,
            input = state.jobName,
            onInputChange = { state.eventSink(NewProjectBasicInfoScreen.Event.OnJobNameChange(it)) },
            placeHolder = stringResource(R.string.project_field_job_name_placeholder),
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_between_fields))
        )
        LogitInputField(
            label = stringResource(R.string.project_field_job_desc_label),
            isRequired = true,
            maxLength = 3000,
            input = state.jobDesc,
            onInputChange = { state.eventSink(NewProjectBasicInfoScreen.Event.OnJobDescChange(it)) },
            placeHolder = stringResource(R.string.project_field_job_desc_placeholder),
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_between_fields))
        )
        LogitInputField(
            label = stringResource(R.string.project_field_talent_label),
            isRequired = false,
            maxLength = 1000,
            input = state.talent,
            onInputChange = { state.eventSink(NewProjectBasicInfoScreen.Event.OnTalentChange(it)) },
            placeHolder = stringResource(R.string.project_field_talent_placeholder),
        )
    }
}

@Preview
@Composable
private fun NewProjectBasicInfoPreview() {
    LogitTheme {
        NewProjectBasicInfo(
            state = NewProjectBasicInfoScreen.State(
                companyName = "",
                jobName = "",
                jobDesc = "",
                talent = "",
                eventSink = {}
            )
        )
    }
}

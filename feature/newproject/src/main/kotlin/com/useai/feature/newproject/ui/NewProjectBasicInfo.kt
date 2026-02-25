package com.useai.feature.newproject.ui

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFormContainer
import com.useai.core.ui.LogitDialog
import com.useai.core.ui.LogitInputField
import com.useai.core.ui.LogitStepper
import com.useai.core.ui.ProgressCheckBox
import com.useai.core.ui.InputFieldLabel
import com.useai.feature.newproject.NewProjectBasicInfoScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@CircuitInject(NewProjectBasicInfoScreen::class, ActivityRetainedComponent::class)
fun NewProjectBasicInfo(
    state: NewProjectBasicInfoScreen.State,
    modifier: Modifier = Modifier
) {
    BackHandler {
        state.eventSink(NewProjectBasicInfoScreen.Event.Back)
    }

    if (state.showExitDialog) {
        LogitDialog(
            onDismissRequest = { state.eventSink(NewProjectBasicInfoScreen.Event.DismissExitDialog) },
            title = stringResource(R.string.project_exit_dialog_text),
            confirmText = stringResource(R.string.project_exit_dialog_confirm),
            onConfirm = { state.eventSink(NewProjectBasicInfoScreen.Event.ConfirmExit) },
            cancelText = stringResource(R.string.project_exit_dialog_cancel),
            onCancel = { state.eventSink(NewProjectBasicInfoScreen.Event.DismissExitDialog) }
        )
    }

    val isButtonEnabled = state.companyName.isNotBlank() &&
        state.jobName.isNotBlank() &&
        state.jobDesc.isNotBlank() &&
        (state.isAlwaysOpen || state.dueDate != null)

    InputFormContainer(
        modifier = modifier.statusBarsPadding(),
        onClickBackButton = {
            state.eventSink(NewProjectBasicInfoScreen.Event.Back)
        },
        bottomButtonText = stringResource(R.string.next),
        onClickBottomButton = {
            state.eventSink(NewProjectBasicInfoScreen.Event.Next)
        },
        bottomButtonEnabled = isButtonEnabled,
        contentScrollEnabled = false,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LogitTheme.colors.white)
                ) {
                    LogitStepper(
                        currentStep = "1",
                        totalStep = "2"
                    )
                    Spacer(
                        modifier = Modifier.height(13.dp)
                    )
                    NewProjectSectionHeader(
                        title = stringResource(R.string.project_form_title_1),
                        desc = stringResource(R.string.project_form_desc_1),
                        onClickLoadExample = {
                            state.eventSink(NewProjectBasicInfoScreen.Event.LoadExample)
                        }
                    )
                    Spacer(
                        modifier = Modifier.height(41.dp)
                    )
                }
            }

            item {
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
            }

            item {
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
            }

            item {
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
            }

            item {
                DeadlineSection(
                    dueDate = state.dueDate,
                    isAlwaysOpen = state.isAlwaysOpen,
                    onDateSelected = { state.eventSink(NewProjectBasicInfoScreen.Event.OnDueDateChange(it)) },
                    onAlwaysOpenChanged = { state.eventSink(NewProjectBasicInfoScreen.Event.OnAlwaysOpenChange(it)) },
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(R.dimen.spacing_between_fields))
                )
            }

            item {
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
                dueDate = null,
                isAlwaysOpen = false,
                showExitDialog = false,
                eventSink = {}
            )
        )
    }
}

@Composable
private fun DeadlineSection(
    dueDate: LocalDate?,
    isAlwaysOpen: Boolean,
    onDateSelected: (LocalDate?) -> Unit,
    onAlwaysOpenChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        InputFieldLabel(
            text = stringResource(R.string.project_field_due_date_label),
            isRequired = true,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_label_to_input)))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = LogitTheme.colors.gray100,
                    shape = RoundedCornerShape(8.dp),
                )
                .clickable(enabled = !isAlwaysOpen) {
                    val initial = dueDate ?: LocalDate.now()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                        },
                        initial.year,
                        initial.monthValue - 1,
                        initial.dayOfMonth,
                    ).show()
                }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            text = dueDate?.format(DUE_DATE_DISPLAY_FORMATTER)
                ?: stringResource(R.string.project_field_due_date_placeholder),
            color = if (dueDate == null) LogitTheme.colors.gray200 else LogitTheme.colors.black,
            style = LogitTheme.typography.body6_1,
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProgressCheckBox(
            checked = isAlwaysOpen,
            text = stringResource(R.string.project_field_due_date_always_open),
            onCheckedChange = onAlwaysOpenChanged,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private val DUE_DATE_DISPLAY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd")

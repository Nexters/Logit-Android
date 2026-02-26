package com.useai.feature.experience.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.LogitDropdownMenu
import com.useai.core.ui.LogitInputField
import com.useai.core.ui.LogitStepper
import com.useai.core.ui.noRippleClickable
import com.useai.feature.experience.ExperienceCreateFormatType
import com.useai.feature.experience.ExperienceCreateScreen
import com.useai.feature.experience.ExperienceCreateStep

@Composable
internal fun ExperienceCreateStarStep(
    state: ExperienceCreateScreen.State,
) {
    LazyColumn {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LogitTheme.colors.white)
            ) {
                LogitStepper(
                    currentStep = ExperienceCreateStep.STAR.step.toString(),
                    totalStep = ExperienceCreateStep.STAR.total.toString(),
                )

                Spacer(modifier = Modifier.padding(top = 13.dp))

                ExperienceCreateSectionHeader(
                    title = stringResource(R.string.experience_create_step2_title),
                    description = stringResource(R.string.experience_create_step2_desc),
                    onClickLoadExample = { state.eventSink(ExperienceCreateScreen.Event.LoadExample) }
                )

                Spacer(modifier = Modifier.padding(top = 20.dp))
            }
        }

        item {
            ExperienceFormatTypeField(
                selectedFormatType = state.selectedFormatType,
                onSelectFormatType = { format ->
                    state.eventSink(ExperienceCreateScreen.Event.SelectFormatType(format))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            ExperienceFormatFields(state = state)
        }
    }
}

@Composable
private fun ExperienceFormatFields(state: ExperienceCreateScreen.State) {
    ExperienceInputField(
        label = stringResource(state.selectedFormatType.firstLabelRes),
        input = state.situation,
        onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeSituation(it)) },
        placeHolder = stringResource(state.selectedFormatType.firstPlaceholderRes),
        minLines = state.selectedFormatType.firstMinLines
    )

    if (state.selectedFormatType.requiresSecondField) {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ExperienceInputField(
            label = stringResource(state.selectedFormatType.secondLabelRes),
            input = state.task,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeTask(it)) },
            placeHolder = stringResource(state.selectedFormatType.secondPlaceholderRes),
            minLines = state.selectedFormatType.secondMinLines
        )
    }

    if (state.selectedFormatType.requiresThirdField) {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ExperienceInputField(
            label = stringResource(state.selectedFormatType.thirdLabelRes),
            input = state.action,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeAction(it)) },
            placeHolder = stringResource(state.selectedFormatType.thirdPlaceholderRes),
            minLines = state.selectedFormatType.thirdMinLines
        )
    }

    if (state.selectedFormatType.requiresFourthField) {
        Spacer(modifier = Modifier.padding(top = 20.dp))
        ExperienceInputField(
            label = stringResource(state.selectedFormatType.fourthLabelRes),
            input = state.result,
            onInputChange = { state.eventSink(ExperienceCreateScreen.Event.ChangeResult(it)) },
            placeHolder = stringResource(state.selectedFormatType.fourthPlaceholderRes),
            minLines = state.selectedFormatType.fourthMinLines
        )
    }
}

@Composable
private fun ExperienceInputField(
    label: String,
    input: String,
    onInputChange: (String) -> Unit,
    placeHolder: String,
    minLines: Int,
) {
    LogitInputField(
        label = label,
        isRequired = true,
        maxLength = 1000,
        input = input,
        onInputChange = onInputChange,
        placeHolder = placeHolder,
        focusedBorder = BorderStroke(1.dp, LogitTheme.colors.primary200),
        minLines = minLines
    )
}

@Composable
private fun ExperienceFormatTypeField(
    selectedFormatType: ExperienceCreateFormatType,
    onSelectFormatType: (ExperienceCreateFormatType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var anchorSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.experience_create_format_label),
            style = LogitTheme.typography.body5_3,
            color = LogitTheme.colors.gray400,
        )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        anchorSize = coordinates.size
                    }
                    .border(
                        width = 1.dp,
                        color = LogitTheme.colors.gray100,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .noRippleClickable { expanded = true }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(selectedFormatType.labelRes),
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray400
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = LogitTheme.colors.gray200
                )
            }

            LogitDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.then(
                    if (anchorSize.width > 0) {
                        Modifier.width(with(density) { anchorSize.width.toDp() })
                    } else {
                        Modifier
                    }
                )
            ) {
                ExperienceCreateFormatType.entries.forEach { formatType ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Text(text = stringResource(formatType.labelRes))
                        },
                        onClick = {
                            expanded = false
                            onSelectFormatType(formatType)
                        }
                    )
                }
            }
        }
    }
}

private val ExperienceCreateFormatType.labelRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_create_format_star
        ExperienceCreateFormatType.PSI -> R.string.experience_create_format_psi
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_create_format_freeform
    }

private val ExperienceCreateFormatType.firstLabelRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_detail_situation
        ExperienceCreateFormatType.PSI -> R.string.experience_create_problem
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_create_freeform_input
    }

private val ExperienceCreateFormatType.firstPlaceholderRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_create_situation_placeholder
        ExperienceCreateFormatType.PSI -> R.string.experience_create_problem_placeholder
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_create_freeform_placeholder
    }

private val ExperienceCreateFormatType.firstMinLines: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> 5
        ExperienceCreateFormatType.PSI -> 4
        ExperienceCreateFormatType.FREEFORM -> 7
    }

private val ExperienceCreateFormatType.requiresSecondField: Boolean
    get() = this != ExperienceCreateFormatType.FREEFORM

private val ExperienceCreateFormatType.secondLabelRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_detail_task
        ExperienceCreateFormatType.PSI -> R.string.experience_create_solution
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_detail_task
    }

private val ExperienceCreateFormatType.secondPlaceholderRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_create_task_placeholder
        ExperienceCreateFormatType.PSI -> R.string.experience_create_solution_placeholder
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_create_task_placeholder
    }

private val ExperienceCreateFormatType.secondMinLines: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> 4
        ExperienceCreateFormatType.PSI -> 4
        ExperienceCreateFormatType.FREEFORM -> 4
    }

private val ExperienceCreateFormatType.requiresThirdField: Boolean
    get() = this != ExperienceCreateFormatType.FREEFORM

private val ExperienceCreateFormatType.thirdLabelRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_detail_action
        ExperienceCreateFormatType.PSI -> R.string.experience_create_insight
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_detail_action
    }

private val ExperienceCreateFormatType.thirdPlaceholderRes: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> R.string.experience_create_action_placeholder
        ExperienceCreateFormatType.PSI -> R.string.experience_create_insight_placeholder
        ExperienceCreateFormatType.FREEFORM -> R.string.experience_create_action_placeholder
    }

private val ExperienceCreateFormatType.thirdMinLines: Int
    get() = when (this) {
        ExperienceCreateFormatType.STAR -> 7
        ExperienceCreateFormatType.PSI -> 5
        ExperienceCreateFormatType.FREEFORM -> 7
    }

private val ExperienceCreateFormatType.requiresFourthField: Boolean
    get() = this == ExperienceCreateFormatType.STAR

private val ExperienceCreateFormatType.fourthLabelRes: Int
    get() = R.string.experience_detail_result

private val ExperienceCreateFormatType.fourthPlaceholderRes: Int
    get() = R.string.experience_create_result_placeholder

private val ExperienceCreateFormatType.fourthMinLines: Int
    get() = 6

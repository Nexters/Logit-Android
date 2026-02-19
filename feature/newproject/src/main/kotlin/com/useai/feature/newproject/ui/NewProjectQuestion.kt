package com.useai.feature.newproject.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.container.LogitOutlinedContainer
import com.useai.core.designsystem.component.textfield.LogitOutlinedTextField
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.project.ProjectQuestionParam
import com.useai.core.ui.InputFormContainer
import com.useai.core.ui.LetterCountInput
import com.useai.core.ui.LogitAddButton
import com.useai.core.ui.LogitFormTitle
import com.useai.core.ui.LogitStepper
import com.useai.feature.newproject.NewProjectQuestionScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(NewProjectQuestionScreen::class, ActivityRetainedComponent::class)
fun NewProjectQuestion(
    modifier: Modifier = Modifier,
    state: NewProjectQuestionScreen.State,
) {
    BackHandler {
        state.eventSink(NewProjectQuestionScreen.Event.Back)
    }

    InputFormContainer(
        modifier = modifier.statusBarsPadding(),
        onClickBackButton = {
            state.eventSink(NewProjectQuestionScreen.Event.Back)
        },
        bottomButtonText = stringResource(R.string.create_project),
        onClickBottomButton = {
            state.eventSink(NewProjectQuestionScreen.Event.FinishClicked)
        },
        bottomButtonEnabled = state.isButtonEnabled,
    ) {
        LogitStepper(
            currentStep = "2",
            totalStep = "2"
        )
        Spacer(Modifier.height(13.dp))
        LogitFormTitle(
            title = stringResource(R.string.project_form_title_2),
            desc = stringResource(R.string.project_form_desc_2),
        )
        Spacer(Modifier.height(75.dp))

        state.questions.forEachIndexed { i, newQuestion ->
            key(i) {
                NewQuestion(
                    question = newQuestion.question,
                    onQuestionChange = { newValue ->
                        state.eventSink(
                            NewProjectQuestionScreen.Event.QuestionChanged(
                                i,
                                newValue
                            )
                        )
                    },
                    maxLength = newQuestion.maxLength,
                    onMaxLengthChange = { newValue ->
                        state.eventSink(
                            NewProjectQuestionScreen.Event.MaxLengthChanged(
                                i,
                                newValue.toIntOrNull() ?: 0
                            )
                        )
                    },
                    onDelete = {
                        state.eventSink(NewProjectQuestionScreen.Event.DeleteQuestionClicked(i))
                    },
                    placeHolder = if (newQuestion.question.isEmpty()) stringResource(R.string.project_field_question, i + 1) else "",
                    isAdditionalQuestion = i > 0,
                )
                Spacer(Modifier.height(8.dp))
            }
        }
        
        LogitAddButton(
            onClick = {
                state.eventSink(NewProjectQuestionScreen.Event.AddQuestionClicked)
            }
        )
    }
}

@Composable
private fun NewQuestion(
    modifier: Modifier = Modifier,
    question: String,
    onQuestionChange: (String) -> Unit,
    maxLength: Int,
    onMaxLengthChange: (String) -> Unit,
    onDelete: () -> Unit,
    placeHolder: String = "",
    isAdditionalQuestion: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogitOutlinedTextField(
            value = question,
            onValueChange = onQuestionChange,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            placeholder = placeHolder,
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
        )
        Spacer(Modifier.width(12.dp))
        LetterCountInput(
            modifier = Modifier
                .fillMaxHeight()
                .width(82.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            letterCount = maxLength.takeIf { it > 0 }?.toString() ?: "",
            onValueChange = onMaxLengthChange
        )
        if (isAdditionalQuestion) {
            Spacer(Modifier.width(12.dp))
            DeleteButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = onDelete,
            )
        }
    }
}

@Composable
private fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    LogitOutlinedContainer(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = LogitIcons.Trash),
                contentDescription = stringResource(R.string.content_description_delete),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun NewProjectQuestionPreview() {
    LogitTheme {
        NewProjectQuestion(
            state = NewProjectQuestionScreen.State(
                questions = listOf(
                    ProjectQuestionParam("", 0),
                    ProjectQuestionParam("", 0),
                ),
                isButtonEnabled = false,
                eventSink = {},
            )
        )
    }
}

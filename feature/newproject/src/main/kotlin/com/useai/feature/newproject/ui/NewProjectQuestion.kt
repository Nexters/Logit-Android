package com.useai.feature.newproject.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.useai.core.model.project.NewQuestionData
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

    val isButtonEnabled = state.questions.any { it.question.isNotBlank() }

    InputFormContainer(
        modifier = modifier,
        onClickBackButton = {
            state.eventSink(NewProjectQuestionScreen.Event.Back)
        },
        bottomButtonText = stringResource(R.string.create_project),
        onClickBottomButton = {
            state.eventSink(NewProjectQuestionScreen.Event.CreateProject)
        },
        bottomButtonEnabled = isButtonEnabled,
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
                    value = newQuestion.question,
                    onValueChange = { newValue ->
                        state.eventSink(
                            NewProjectQuestionScreen.Event.OnQuestionChange(
                                i,
                                newQuestion.copy(question = newValue)
                            )
                        )
                    },
                    onDelete = {
                        state.eventSink(NewProjectQuestionScreen.Event.DeleteQuestion(i))
                    },
                    placeHolder = if (newQuestion.question.isEmpty()) stringResource(R.string.project_field_question, i + 1) else "",
                    isAdditionalQuestion = i > 0,
                )
                Spacer(Modifier.height(8.dp))
            }
        }
        
        LogitAddButton(
            onClick = {
                state.eventSink(NewProjectQuestionScreen.Event.AddQuestion)
            }
        )
    }
}

@Composable
private fun NewQuestion(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onDelete: () -> Unit = {},
    placeHolder: String = "",
    isAdditionalQuestion: Boolean = false,
) {
    var letterCount by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogitOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
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
            letterCount = letterCount,
            onValueChange = { letterCount = it }
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
    NewProjectQuestion(
        state = NewProjectQuestionScreen.State(
            questions = listOf(NewQuestionData()),
            eventSink = {},
        )
    )
}

@Preview
@Composable
private fun AdditionalQuestionPreview() {
    LogitTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LogitTheme.colors.white)
        ) {
            NewQuestion(
                isAdditionalQuestion = true,
            )
        }
    }
}

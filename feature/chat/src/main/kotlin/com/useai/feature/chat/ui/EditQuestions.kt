package com.useai.feature.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.textfield.LogitOutlinedTextField
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFormContainer
import com.useai.feature.chat.EditQuestionsScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(EditQuestionsScreen::class, ActivityRetainedComponent::class)
fun EditQuestions(
    state: EditQuestionsScreen.State,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        state.eventSink(EditQuestionsScreen.Event.Back)
    }

    InputFormContainer(
        modifier = modifier.statusBarsPadding(),
        onClickBackButton = {
            state.eventSink(EditQuestionsScreen.Event.Back)
        },
        bottomButtonText = stringResource(R.string.chat_edit_questions_submit),
        onClickBottomButton = {
            state.eventSink(EditQuestionsScreen.Event.Submit)
        },
        bottomButtonEnabled = state.isSubmitEnabled,
        contentScrollEnabled = false,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.chat_edit_questions_title),
                style = LogitTheme.typography.body3_1,
                color = LogitTheme.colors.black,
            )

            Text(
                text = stringResource(R.string.chat_edit_questions_desc),
                style = LogitTheme.typography.body6_1,
                color = LogitTheme.colors.gray300,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                state.questions.forEachIndexed { index, question ->
                    val isQuestionError = state.showValidationErrors && question.title.isBlank()
                    val isCountError = state.showValidationErrors && question.maxLength <= 0

                    QuestionEditRow(
                        index = index,
                        question = question,
                        isQuestionError = isQuestionError,
                        isCountError = isCountError,
                        onQuestionChange = {
                            state.eventSink(EditQuestionsScreen.Event.ChangeQuestion(index, it))
                        },
                        onMaxLengthChange = {
                            state.eventSink(EditQuestionsScreen.Event.ChangeMaxLength(index, it))
                        },
                        onDelete = {
                            state.eventSink(EditQuestionsScreen.Event.DeleteQuestion(index))
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                AddQuestionButton(
                    onClick = {
                        state.eventSink(EditQuestionsScreen.Event.AddQuestion)
                    }
                )
            }
        }
    }
}

@Composable
private fun QuestionEditRow(
    index: Int,
    question: EditQuestionsScreen.EditableQuestion,
    isQuestionError: Boolean,
    isCountError: Boolean,
    onQuestionChange: (String) -> Unit,
    onMaxLengthChange: (String) -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogitOutlinedTextField(
            value = question.title,
            onValueChange = onQuestionChange,
            placeholder = stringResource(R.string.chat_edit_question_placeholder, index + 1),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            border = BorderStroke(
                width = 1.dp,
                color = if (isQuestionError) LogitTheme.colors.alert else LogitTheme.colors.gray100
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            maxLines = 1,
            minLines = 1,
        )

        Spacer(modifier = Modifier.width(8.dp))

        MaxLengthInput(
            value = if (question.maxLength == 0) "" else question.maxLength.toString(),
            isError = isCountError,
            onValueChange = onMaxLengthChange,
            modifier = Modifier
                .fillMaxHeight()
                .width(78.dp)
        )

        if (index > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            DeleteQuestionButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
private fun MaxLengthInput(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = LogitTheme.colors.white,
        border = BorderStroke(
            width = 1.dp,
            color = if (isError) LogitTheme.colors.alert else LogitTheme.colors.gray100
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        onValueChange(input)
                    }
                },
                modifier = Modifier.weight(1f),
                textStyle = LogitTheme.typography.body6_1.copy(color = LogitTheme.colors.gray400),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.chat_edit_count_placeholder),
                                style = LogitTheme.typography.body6_1,
                                color = LogitTheme.colors.gray100,
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Text(
                text = stringResource(R.string.letter_count_unit),
                style = LogitTheme.typography.body6_1,
                color = LogitTheme.colors.gray300,
            )
        }
    }
}

@Composable
private fun AddQuestionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(onClick = onClick),
        color = LogitTheme.colors.primary50,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+",
                style = LogitTheme.typography.body3_1,
                color = LogitTheme.colors.primary100,
                modifier = Modifier.padding(end = 6.dp)
            )
            Text(
                text = stringResource(R.string.chat_edit_questions_add),
                style = LogitTheme.typography.body6_2,
                color = LogitTheme.colors.gray300,
            )
        }
    }
}

@Composable
private fun DeleteQuestionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(44.dp)
            .clickable(onClick = onClick),
        color = LogitTheme.colors.white,
        border = BorderStroke(1.dp, LogitTheme.colors.gray100),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "-",
                style = LogitTheme.typography.body3_1,
                color = LogitTheme.colors.gray300,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditQuestionsPreview() {
    LogitTheme {
        EditQuestions(
            state = EditQuestionsScreen.State(
                questions = listOf(
                    EditQuestionsScreen.EditableQuestion("1", "[필수] 본 직무에 지원하...", 1000, ""),
                    EditQuestionsScreen.EditableQuestion("2", "[필수] 본 직무에 지원하...", 1000, ""),
                    EditQuestionsScreen.EditableQuestion("", "", 0, ""),
                ),
                isSubmitting = false,
                isSubmitEnabled = false,
                showValidationErrors = true,
            )
        )
    }
}

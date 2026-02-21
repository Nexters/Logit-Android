package com.useai.feature.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.InputFieldLabel
import com.useai.core.ui.InputFormContainer
import com.useai.core.ui.LetterCountInput
import com.useai.core.ui.LogitFormTitle
import com.useai.core.ui.LogitInputField
import com.useai.feature.chat.NewQuestionScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(NewQuestionScreen::class, ActivityRetainedComponent::class)
fun NewQuestion(
    modifier: Modifier = Modifier,
    state: NewQuestionScreen.State,
) {
    BackHandler {
        state.eventSink(NewQuestionScreen.Event.Back)
    }

    InputFormContainer(
        modifier = modifier.statusBarsPadding(),
        onClickBackButton = { state.eventSink(NewQuestionScreen.Event.Back) },
        bottomButtonText = stringResource(R.string.question_form_confirm),
        onClickBottomButton = { state.eventSink(NewQuestionScreen.Event.ConfirmClicked) },
        bottomButtonEnabled = state.isButtonEnabled,
    ) {
        LogitFormTitle(
            title = stringResource(R.string.question_form_title),
            desc = stringResource(R.string.question_form_desc),
        )
        Spacer(Modifier.height(41.dp))
        LogitInputField(
            label = stringResource(R.string.question_field_label),
            isRequired = true,
            input = state.question,
            onInputChange = {
                state.eventSink(
                    NewQuestionScreen.Event.QuestionChanged(it)
                )
            },
            placeHolder = stringResource(R.string.question_field_placeholder),
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_between_fields)))
        MaxLengthField(
            maxLength = state.maxLength,
            onValueChange = {
                state.eventSink(
                    NewQuestionScreen.Event.MaxLengthChanged(
                        it.toIntOrNull() ?: 0
                    )
                )
            }
        )
    }
}

@Composable
private fun MaxLengthField(
    modifier: Modifier = Modifier,
    maxLength: Int,
    onValueChange: (String) -> Unit,
) {
    Column (
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputFieldLabel(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.question_max_length_field_label),
                isRequired = true,
            )
        }
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_label_to_input))
        )
        LetterCountInput(
            modifier = Modifier.fillMaxWidth(),
            letterCount = if (maxLength == 0) "" else maxLength.toString(),
            onValueChange = onValueChange,
        )
    }
}

@Preview
@Composable
private fun NewQuestionPreview() {
    LogitTheme {
        NewQuestion(
            state = NewQuestionScreen.State(
                question = "",
                maxLength = 0,
                isButtonEnabled = false,
            )
        )
    }
}

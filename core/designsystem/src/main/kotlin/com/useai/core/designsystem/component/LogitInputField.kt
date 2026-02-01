package com.useai.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.textfield.LogitOutlinedTextField
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitInputField(
    modifier: Modifier = Modifier,
    label: String,
    isRequired: Boolean,
    maxLength: String,
    input: String = "",
    onInputChange: (String) -> Unit = {},
    placeHolder: String,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    Column (
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputFieldLabel(
                modifier = Modifier.weight(1f),
                text = label,
                isRequired = isRequired,
            )
            NumberOfLetters(
                currentNumber = input.length.toString(),
                maxNumber = maxLength,
            )
        }
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_label_to_input))
        )
        LogitOutlinedTextField(
            value = input,
            onValueChange = {
                onInputChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeHolder,
            maxLines = maxLines,
            minLines = minLines,
        )
    }
}

@Composable
private fun InputFieldLabel(
    modifier: Modifier = Modifier,
    text: String,
    isRequired: Boolean = true,
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = text,
            color = LogitTheme.colors.black,
            style = LogitTheme.typography.body5_3,
        )
        if (isRequired) {
            Text(
                text = "*",
                color = LogitTheme.colors.alert,
                style = LogitTheme.typography.body5_3,
            )
        }
    }
}

@Composable
private fun NumberOfLetters(
    modifier: Modifier = Modifier,
    currentNumber: String,
    maxNumber: String,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = currentNumber,
            color = LogitTheme.colors.black,
            style = LogitTheme.typography.body6_1,
        )
        Text(
            text = " / $maxNumber",
            color = LogitTheme.colors.gray200,
            style = LogitTheme.typography.body6_1,
        )
    }
}

@Preview
@Composable
private fun LogitInputFieldPreview() {
    LogitTheme {
        Box(
            modifier = Modifier
                .background(LogitTheme.colors.white)
        ) {
            LogitInputField(
                label = "기업명",
                isRequired = true,
                maxLength = "100",
                placeHolder = "기업명",
            )
        }
    }
}

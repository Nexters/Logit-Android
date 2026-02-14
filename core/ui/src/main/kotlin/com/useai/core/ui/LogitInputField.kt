package com.useai.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
    maxLength: Int = 0,
    input: String = "",
    onInputChange: (String) -> Unit = {},
    placeHolder: String,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    focusedBorder: BorderStroke? = null,
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
            if (maxLength > 0) {
                LetterCounter(
                    currentCount = input.length.toString(),
                    maxCount = maxLength.toString(),
                )
            }
        }
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_label_to_input))
        )
        LogitOutlinedTextField(
            value = input,
            onValueChange = onInputChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeHolder,
            maxLength = maxLength,
            focusedBorder = focusedBorder,
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.spacing_input_field_horizontal),
                vertical = dimensionResource(R.dimen.spacing_input_field_vertical)
            ),
            maxLines = maxLines,
            minLines = minLines,
        )
    }
}

@Composable
fun InputFieldLabel(
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
private fun LetterCounter(
    modifier: Modifier = Modifier,
    currentCount: String,
    maxCount: String,
) {
    Row(
        modifier = modifier,
    ) {
        Text(
            text = currentCount,
            color = LogitTheme.colors.black,
            style = LogitTheme.typography.body6_1,
        )
        Text(
            text = " / $maxCount",
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
                maxLength = 100,
                placeHolder = "예) 로짓 컴퍼니",
            )
        }
    }
}

@Preview
@Composable
private fun LogitInputFieldWithNoMaxLengthPreview() {
    LogitTheme {
        Box(
            modifier = Modifier
                .background(LogitTheme.colors.white)
        ) {
            LogitInputField(
                label = "기업명",
                isRequired = false,
                placeHolder = "예) 로짓 컴퍼니",
            )
        }
    }
}

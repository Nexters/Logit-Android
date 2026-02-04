package com.useai.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.container.LogitOutlinedContainer
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LetterCountInput(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = dimensionResource(R.dimen.spacing_input_field_horizontal),
    verticalPadding: Dp = dimensionResource(R.dimen.spacing_input_field_vertical),
    letterCount: String = "",
    onValueChange: (String) -> Unit,
) {
    LogitOutlinedContainer(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding,
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = letterCount,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                textStyle = LogitTheme.typography.body6_1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = { innerTextField ->
                    Box {
                        if (letterCount.isEmpty()) {
                            Text(
                                text = "글자수",
                                style = LogitTheme.typography.body6_1,
                                color = LogitTheme.colors.gray200,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = "자",
                style = LogitTheme.typography.body6_1,
                color = LogitTheme.colors.primary500,
            )
        }
    }
}

@Preview
@Composable
private fun LetterCountInputPreview() {
    LogitTheme {
        LetterCountInput(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {},
        )
    }
}

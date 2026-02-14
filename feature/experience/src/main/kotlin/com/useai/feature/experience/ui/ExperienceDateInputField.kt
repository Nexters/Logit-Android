package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
internal fun ExperienceDateInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "YYYY. MM. DD",
    enabled: Boolean = true,
) {
    var fieldValue by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (value != fieldValue.text) {
            fieldValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    BasicTextField(
        value = fieldValue,
        onValueChange = { newValue ->
            val digitCursor = countDigitsBeforeCursor(newValue.text, newValue.selection.start)
            val formatted = formatDateInput(newValue.text)
            val nextCursor = cursorIndexForDigitCount(formatted, digitCursor)

            fieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(nextCursor)
            )
            onValueChange(formatted)
        },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = LogitTheme.typography.body5_4.copy(color = LogitTheme.colors.black),
        cursorBrush = SolidColor(LogitTheme.colors.black),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = LogitTheme.colors.white, shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = LogitTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(PaddingValues(horizontal = 18.dp, vertical = 10.dp)),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                if (fieldValue.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = LogitTheme.typography.body5_4,
                        color = LogitTheme.colors.gray100
                    )
                }
                innerTextField()
            }
        }
    )
}

private fun formatDateInput(raw: String): String {
    val digits = raw.filter { it.isDigit() }.take(8)
    val builder = StringBuilder()
    digits.forEachIndexed { index, c ->
        builder.append(c)
        if (index == 3 || index == 5) {
            builder.append(". ")
        }
    }
    return builder.toString()
}

private fun countDigitsBeforeCursor(text: String, cursor: Int): Int {
    val clampedCursor = cursor.coerceIn(0, text.length)
    return text.take(clampedCursor).count { it.isDigit() }
}

private fun cursorIndexForDigitCount(formatted: String, digitCount: Int): Int {
    if (digitCount <= 0) return 0

    var seenDigits = 0
    formatted.forEachIndexed { index, c ->
        if (c.isDigit()) {
            seenDigits += 1
            if (seenDigits == digitCount) {
                val baseCursor = index + 1
                // When "." is auto-inserted after YYYY/MM, place cursor after ". ".
                return if (
                    (digitCount == 4 || digitCount == 6) &&
                    formatted.getOrNull(baseCursor) == '.' &&
                    formatted.getOrNull(baseCursor + 1) == ' '
                ) {
                    baseCursor + 2
                } else {
                    baseCursor
                }
            }
        }
    }
    return formatted.length
}

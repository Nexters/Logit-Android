package com.useai.core.designsystem.component.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme
import kotlin.contracts.ExperimentalContracts

@OptIn(ExperimentalContracts::class)
@Composable
fun LogitOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
    border: BorderStroke = BorderStroke(1.dp, LogitTheme.colors.gray100),
    shape: Shape = RoundedCornerShape(8.dp),
    textStyle: TextStyle = LogitTheme.typography.body5_4,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(LogitTheme.colors.black)
) {
    val showPlaceholder = remember(value, placeholder) {
        placeholder != null && value.isEmpty()
    }

    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        modifier = Modifier
            .clip(shape)
            .background(color = LogitTheme.colors.white, shape = shape)
            .border(border = border, shape = shape)
            .then(modifier),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = false,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = { field ->

            Box(
                modifier = Modifier.padding(contentPadding)
            ) {
                if (showPlaceholder) {
                    Text(
                        text = placeholder.orEmpty(),
                        style = textStyle,
                        color = LogitTheme.colors.gray100
                    )
                }
                field()
            }
        }
    )
}

@Composable
@Preview
private fun LogitOutlinedTextFieldEmptyPreview() {
    LogitOutlinedTextField(placeholder = "예) 가나다라마바사", value = "", onValueChange = {})
}

@Composable
@Preview
private fun LogitOutlinedTextFieldPreview() {
    LogitOutlinedTextField(value = "가나다라마바사", onValueChange = {})
}

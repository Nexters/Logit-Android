package com.useai.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = LogitButtonDefaults.Shape,
    colors: ButtonColors = LogitButtonDefaults.buttonColors(
        containerColor = LogitTheme.colors.primary50,
        contentColor = LogitTheme.colors.gray300
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = LogitButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable (RowScope.() -> Unit)
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun LogitTertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = LogitButtonDefaults.TextStyle,
    shape: Shape = LogitButtonDefaults.Shape,
    colors: ButtonColors = LogitButtonDefaults.buttonColors(
        containerColor = LogitTheme.colors.primary50,
        contentColor = LogitTheme.colors.gray300
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = LogitButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {

    LogitTertiaryButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Text(text = text, style = textStyle)
    }
}

@Preview
@Composable
private fun LogitTertiaryButtonPreview(){
    LogitTertiaryButton(text = "Tertiary", onClick = {})
}

@Preview
@Composable
private fun LogitDisabledButtonPreview(){
    LogitTertiaryButton(text = "Tertiary", onClick = {}, enabled = false)
}

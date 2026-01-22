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
fun LogitSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = LogitButtonDefaults.TextStyle,
    shape: Shape = LogitButtonDefaults.Shape,
    colors: ButtonColors = LogitButtonDefaults.buttonColors(
        containerColor = LogitTheme.colors.primary20,
        contentColor = LogitTheme.colors.primary200
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = LogitButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {

    LogitSecondaryButton(
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

@Composable
fun LogitSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = LogitButtonDefaults.Shape,
    colors: ButtonColors = LogitButtonDefaults.buttonColors(
        containerColor = LogitTheme.colors.primary20,
        contentColor = LogitTheme.colors.primary200
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

@Preview
@Composable
private fun LogitSecondaryButtonPreview(){
    LogitSecondaryButton(text = "Secondary", onClick = {})
}

@Preview
@Composable
private fun LogitDisabledButtonPreview(){
    LogitSecondaryButton(text = "Secondary", onClick = {}, enabled = false)
}


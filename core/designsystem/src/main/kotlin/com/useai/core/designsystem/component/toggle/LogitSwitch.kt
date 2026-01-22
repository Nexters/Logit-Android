package com.useai.core.designsystem.component.toggle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

private val ContentPadding = 2.dp
private val Radius = 8.dp
private val Width = 32.dp
private val Height = 20.dp

@Composable
fun LogitSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    colors: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = LogitTheme.colors.white,
        checkedTrackColor = LogitTheme.colors.primary600,
        uncheckedThumbColor = LogitTheme.colors.white,
        uncheckedTrackColor = LogitTheme.colors.gray100,
        uncheckedBorderColor = Color.Transparent
    ),
) {

    val thumbAnimatePosition = animateFloatAsState(
        targetValue = with(LocalDensity.current) {
            if (checked) (Width - Radius - ContentPadding).toPx() else (ContentPadding + Radius).toPx()
        },
        label = "thumbAnimatePosition"
    )

    Canvas(
        modifier = Modifier.size(width = Width, height = Height)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onCheckedChange?.invoke(!checked)
                    }
                )
            }.then(modifier)
    ) {

        drawRoundRect(
            color = if (checked) colors.checkedTrackColor else colors.uncheckedTrackColor,
            cornerRadius = CornerRadius(50f, 50f)
        )

        drawCircle(
            color = if (checked) colors.checkedThumbColor else colors.uncheckedThumbColor,
            radius = Radius.toPx(),
            center = Offset(
                x = thumbAnimatePosition.value,
                y = size.height / 2
            )
        )
    }
}

@Composable
@Preview
private fun LogitSwitchCheckedPreview() {
    LogitSwitch(checked = true, onCheckedChange = null)
}

@Composable
@Preview
private fun LogitSwitchUncheckedPreview() {
    LogitSwitch(checked = false, onCheckedChange = null)
}

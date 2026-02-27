package com.useai.core.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return

    val density = LocalDensity.current
    val popupPositionProvider = remember(density) {
        LogitDropdownMenuPositionProvider(density = density)
    }

    Popup(
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(14.dp),
            color = LogitTheme.colors.white,
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                content = content,
            )
        }
    }
}

@Composable
fun LogitDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    iconTint: Color = LogitTheme.colors.primary600,
    contentPadding: PaddingValues = PaddingValues(
        top = 12.dp,
        bottom = 12.dp,
        start = 20.dp,
        end = 20.dp
    ),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 48.dp)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                )
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                style = LogitTheme.typography.body5_3,
                color = if (enabled) LogitTheme.colors.primary600 else LogitTheme.colors.gray200,
            )
        }
    }
}

private class LogitDropdownMenuPositionProvider(
    private val density: Density,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val horizontalMargin = with(density) { 8.dp.roundToPx() }
        val verticalMargin = with(density) { 8.dp.roundToPx() }
        val verticalOffset = with(density) { 4.dp.roundToPx() }

        val preferredX = when (layoutDirection) {
            LayoutDirection.Ltr -> anchorBounds.right - popupContentSize.width
            LayoutDirection.Rtl -> anchorBounds.left
        }
        val maxX = (windowSize.width - popupContentSize.width - horizontalMargin)
            .coerceAtLeast(horizontalMargin)
        val x = preferredX.coerceIn(horizontalMargin, maxX)

        val preferredBelowY = anchorBounds.bottom + verticalOffset
        val preferredAboveY = anchorBounds.top - popupContentSize.height - verticalOffset
        val maxY = (windowSize.height - popupContentSize.height - verticalMargin).coerceAtLeast(verticalMargin)
        val y = if (preferredBelowY + popupContentSize.height <= windowSize.height - verticalMargin) {
            preferredBelowY
        } else {
            preferredAboveY
        }.coerceIn(verticalMargin, maxY)

        return IntOffset(x, y)
    }
}

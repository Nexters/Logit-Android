package com.useai.core.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        containerColor = LogitTheme.colors.white,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        content = content,
    )
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
        end = 18.dp
    ),
) {
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Text(
                text = text,
                style = LogitTheme.typography.body5_3,
                color = if (enabled) LogitTheme.colors.primary600 else LogitTheme.colors.gray200,
            )
        },
        leadingIcon = {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp),
                )
            }
        },
        onClick = onClick,
        enabled = enabled,
        contentPadding = contentPadding,
    )
}

package com.useai.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitLoadingAnimation(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = LogitTheme.colors.primary100,
        strokeWidth = 3.dp
    )
}

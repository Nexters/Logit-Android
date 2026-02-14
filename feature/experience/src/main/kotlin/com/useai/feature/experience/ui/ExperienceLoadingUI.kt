package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
internal fun ExperienceLoadingUI(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.gray20),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.experience_loading),
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.gray300
        )
    }
}

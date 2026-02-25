package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.LogitPageLoadingView

@Composable
internal fun ExperienceLoadingUI(
    modifier: Modifier = Modifier,
) {
    LogitPageLoadingView(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white)
            .statusBarsPadding()
    )
}

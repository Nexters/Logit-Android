package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme

@Composable
internal fun ExperienceLoadFailedUI(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.experience_load_failed),
                style = LogitTheme.typography.body6_1,
                color = LogitTheme.colors.gray300,
            )
            Spacer(modifier = Modifier.height(12.dp))
            LogitPrimaryButton(
                text = stringResource(R.string.experience_retry),
                onClick = onRetry,
                modifier = Modifier.width(140.dp)
            )
        }
    }
}

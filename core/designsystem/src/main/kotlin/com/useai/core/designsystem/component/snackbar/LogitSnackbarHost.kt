package com.useai.core.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

private data class LogitSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    @param:DrawableRes val iconResId: Int,
    override val withDismissAction: Boolean,
    override val duration: SnackbarDuration,
) : SnackbarVisuals

suspend fun SnackbarHostState.showLogitSnackbar(
    message: String,
    actionText: String,
    @DrawableRes iconResId: Int,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult {
    return showSnackbar(
        visuals = LogitSnackbarVisuals(
            message = message,
            actionLabel = actionText,
            iconResId = iconResId,
            withDismissAction = withDismissAction,
            duration = duration
        )
    )
}

suspend fun SnackbarHostState.showLogitSnackbar(
    message: String,
    @DrawableRes iconResId: Int,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult {
    return showSnackbar(
        visuals = LogitSnackbarVisuals(
            message = message,
            actionLabel = null,
            iconResId = iconResId,
            withDismissAction = withDismissAction,
            duration = duration
        )
    )
}

@Composable
fun LogitSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { snackbarData ->
        val visuals = snackbarData.visuals as? LogitSnackbarVisuals
        val actionText = visuals?.actionLabel ?: snackbarData.visuals.actionLabel
        val iconResId = visuals?.iconResId ?: R.drawable.ic_add

        if (!actionText.isNullOrBlank()) {
            LogitSnackbar(
                message = snackbarData.visuals.message,
                actionText = actionText,
                iconResId = iconResId,
                onActionClick = snackbarData::performAction,
            )
        } else {
            LogitSnackbar(
                message = snackbarData.visuals.message,
                iconResId = iconResId,
            )
        }
    }
}

@Preview(name = "Token Snackbar", showBackground = true)
@Composable
private fun TokenSnackbarHostPreview() {
    LogitTheme {
        val hostState = remember { SnackbarHostState() }
        val message = stringResource(R.string.token_attendance_banner, 3)

        LaunchedEffect(hostState, message) {
            hostState.showLogitSnackbar(
                message = message,
                iconResId = R.drawable.ic_complete,
                duration = SnackbarDuration.Indefinite,
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LogitSnackbarHost(
                hostState = hostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(20.dp),
            )
        }
    }
}

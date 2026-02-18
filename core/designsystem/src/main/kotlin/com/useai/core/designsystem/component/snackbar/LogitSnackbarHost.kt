package com.useai.core.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.useai.core.designsystem.R

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

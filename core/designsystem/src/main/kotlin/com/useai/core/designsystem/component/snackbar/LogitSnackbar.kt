package com.useai.core.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitSnackbar(
    message: String,
    actionText: String,
    onActionClick: () -> Unit,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
) {
    LogitSnackbarInternal(
        message = message,
        iconResId = iconResId,
        modifier = modifier,
        actionText = actionText,
        onActionClick = onActionClick,
    )
}

@Composable
fun LogitSnackbar(
    message: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
) {
    LogitSnackbarInternal(
        message = message,
        iconResId = iconResId,
        modifier = modifier,
        actionText = null,
        onActionClick = null,
    )
}

@Composable
private fun LogitSnackbarInternal(
    message: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier,
    actionText: String?,
    onActionClick: (() -> Unit)?,
) {
    Surface(
        modifier = modifier,
        color = LogitTheme.colors.primary400,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = message,
                style = LogitTheme.typography.body5_4,
                color = LogitTheme.colors.white,
                modifier = Modifier.weight(1f)
            )

            if (!actionText.isNullOrBlank() && onActionClick != null) {
                Surface(
                    color = LogitTheme.colors.primary500,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxHeight().clickable(onClick = onActionClick)
                ) {
                    Text(
                        text = actionText,
                        style = LogitTheme.typography.body7_3,
                        color = LogitTheme.colors.white,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogitSnackbarPreview() {
    LogitTheme {
        LogitSnackbar(
            message = "Resume has been updated",
            actionText = "View now",
            onActionClick = {},
            iconResId = R.drawable.ic_add
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LogitSnackbarWithoutActionPreview() {
    LogitTheme {
        LogitSnackbar(
            message = "Resume has been updated",
            iconResId = R.drawable.ic_add
        )
    }
}

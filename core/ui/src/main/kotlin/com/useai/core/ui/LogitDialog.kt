package com.useai.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitButtonDefaults
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    confirmText: String,
    onConfirm: () -> Unit,
    cancelText: String,
    onCancel: (() -> Unit),
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = modifier
                .width(295.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LogitTheme.colors.white)
                .padding(
                    vertical = 24.dp,
                    horizontal = 22.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_alert),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_dialog_vertical)))
            Text(
                text = title,
                style = LogitTheme.typography.body5_2,
                color = LogitTheme.colors.black,
                textAlign = TextAlign.Center
            )
            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray200,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_dialog_vertical)))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CancelButton(
                    modifier = Modifier.weight(1f),
                    text = cancelText,
                    onClick = onCancel,
                )
                Spacer(modifier = Modifier.width(12.dp))
                ConfirmButton(
                    modifier = Modifier.weight(1f),
                    text = confirmText,
                    onClick = onConfirm,
                )
            }
        }
    }
}

@Composable
private fun CancelButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    LogitPrimaryButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        textStyle = LogitTheme.typography.body8_1,
        shape = RoundedCornerShape(8.dp),
        colors = LogitButtonDefaults.buttonColors(
            containerColor = LogitTheme.colors.gray50,
            contentColor = LogitTheme.colors.gray400,
        ),
        contentPadding = PaddingValues(
            vertical = 11.dp,
        ),
    )
}

@Composable
private fun ConfirmButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    LogitPrimaryButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        textStyle = LogitTheme.typography.body8_1,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(
            vertical = 11.dp,
        ),
    )
}

@Preview
@Composable
private fun LogitDialogPreview() {
    LogitTheme {
        LogitDialog(
            onDismissRequest = {},
            title = "프로젝트 생성을 취소하시겠어요?",
            confirmText = "그만하기",
            onConfirm = {},
            cancelText = "계속하기",
            onCancel = {}
        )
    }
}

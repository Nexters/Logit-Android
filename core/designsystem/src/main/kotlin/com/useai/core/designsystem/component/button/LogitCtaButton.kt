package com.useai.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitCtaButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    LogitPrimaryButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        textStyle = LogitTheme.typography.body3_1,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
    )
}

@Preview
@Composable
private fun LogitCtaButtonPreview() {
    LogitCtaButton(
        modifier = Modifier.fillMaxWidth(),
        text = "다음으로",
        onClick = {},
        enabled = true
    )
}

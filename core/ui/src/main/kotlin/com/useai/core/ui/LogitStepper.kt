package com.useai.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitStepper(
    modifier: Modifier = Modifier,
    currentStep: String,
    totalStep: String,
) {
    Box(
        modifier = modifier
            .width(40.dp)
            .height(28.dp)
            .background(
                color = LogitTheme.colors.primary20,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = currentStep,
                color = LogitTheme.colors.gray400,
                style = LogitTheme.typography.body7_1,
            )
            Text(
                text = "/$totalStep",
                color = LogitTheme.colors.gray400,
                style = LogitTheme.typography.body7_3,
            )
        }
    }
}

@Preview
@Composable
private fun LogitStepperPreview() {
    LogitStepper(
        currentStep = "1",
        totalStep = "2"
    )
}

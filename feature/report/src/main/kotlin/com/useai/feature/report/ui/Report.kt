package com.useai.feature.report.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.feature.report.ReportScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(ReportScreen::class, ActivityRetainedComponent::class)
fun Report(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Text("Report screen")
    }
}

@Preview
@Composable
private fun ReportPreview() {
    Report()
}

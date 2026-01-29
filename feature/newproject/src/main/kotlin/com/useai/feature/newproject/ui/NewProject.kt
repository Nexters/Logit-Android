package com.useai.feature.newproject.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.feature.newproject.NewProjectScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(NewProjectScreen::class, ActivityRetainedComponent::class)
fun NewProject(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "NewProject screen")
    }
}

@Preview
@Composable
private fun NewProjectPreview() {
    NewProject()
}

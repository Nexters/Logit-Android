package com.useai.feature.experience.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.feature.experience.ExperienceScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(ExperienceScreen::class, ActivityRetainedComponent::class)
fun ExperienceList(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Text("Experience screen")
    }
}

@Preview
@Composable
private fun ExperienceListPreview() {
    ExperienceList()
}

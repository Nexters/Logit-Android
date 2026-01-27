package com.useai.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.feature.home.HomeScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
fun Home(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "Home screen")
    }
}

@Preview
@Composable
private fun HomePreview() {
    Home()
}

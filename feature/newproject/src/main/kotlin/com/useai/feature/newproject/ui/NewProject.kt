package com.useai.feature.newproject.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.component.appbar.PopUpTitle
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.newproject.NewProjectScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@CircuitInject(NewProjectScreen::class, ActivityRetainedComponent::class)
fun NewProject(
    state: NewProjectScreen.State,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LogitTheme.colors.white,
        topBar = {
            PopUpTitle(
                onClick = {
                    state.eventSink(NewProjectScreen.Event.Back)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "NewProject screen content")
        }
    }
}

@Preview
@Composable
private fun NewProjectPreview() {
    LogitTheme {
        NewProject(
            state = NewProjectScreen.State { }
        )
    }
}

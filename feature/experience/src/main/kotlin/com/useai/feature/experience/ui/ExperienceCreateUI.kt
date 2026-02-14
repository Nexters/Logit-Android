package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.experience.ExperienceCreateScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(ExperienceCreateScreen::class, ActivityRetainedComponent::class)
fun ExperienceCreateUI(
    state: ExperienceCreateScreen.State,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.content_description_navigate_back),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.gray300,
            modifier = Modifier.clickable {
                state.eventSink(ExperienceCreateScreen.Event.Back)
            }
        )

        Text(
            text = stringResource(R.string.experience_register),
            style = LogitTheme.typography.body1,
            color = LogitTheme.colors.black,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

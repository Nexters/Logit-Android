package com.useai.feature.account.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.account.AccountScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(AccountScreen::class, ActivityRetainedComponent::class)
fun Account(
    modifier: Modifier = Modifier,
    state: AccountScreen.State,
) {
    Box(
        modifier.fillMaxSize()
    ) {
        Text("Account screen")
    }
}

@Preview
@Composable
private fun AccountPreview() {
    LogitTheme {
        Scaffold(
            containerColor = LogitTheme.colors.white
        ) { paddingValues ->
            Account(
                modifier = Modifier.padding(paddingValues),
                state = AccountScreen.State()
            )
        }
    }
}

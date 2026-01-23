package com.useai.feature.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.feature.chat.ChatScreen
import com.useai.feature.chat.ui.chatting.ChatChattingUI
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(ChatScreen::class, ActivityRetainedComponent::class)
@Composable
fun ChatUI(state: ChatScreen.State, modifier: Modifier) {

    Column(modifier = modifier) {
        when(state) {
            is ChatScreen.State.Chatting -> {
                ChatChattingUI(state, Modifier.fillMaxSize())
            }

            is ChatScreen.State.Letter -> {
                TODO("ChatLetterUI")
            }

            is ChatScreen.State.Loading -> {
                TODO("ChatLoadingUI")
            }

            is ChatScreen.State.LoadFailed -> {
                TODO("ChatLoadFailedUI")
            }
        }
    }
}

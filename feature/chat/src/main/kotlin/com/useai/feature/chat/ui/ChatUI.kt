package com.useai.feature.chat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.chat.ChatScreen
import com.useai.feature.chat.ChatScreenCategory
import com.useai.feature.chat.ui.chatting.ChatChattingUI
import com.useai.feature.chat.ui.letter.ChatLetterUI
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(ChatScreen::class, ActivityRetainedComponent::class)
@Composable
fun ChatUI(state: ChatScreen.State, modifier: Modifier) {

    val chattingListState = rememberLazyListState()

    Column(modifier = modifier.systemBarsPadding()) {
        when (state) {
            is ChatScreen.State.Success -> {
                if (state.currentCategory == ChatScreenCategory.CHATTING) {
                    ChatChattingUI(
                        state,
                        Modifier.fillMaxSize().background(color = LogitTheme.colors.white),
                        chattingListState
                    )
                } else if (state.currentCategory == ChatScreenCategory.LETTER) {
                    ChatLetterUI(
                        state,
                        Modifier.fillMaxSize().background(color = LogitTheme.colors.white)
                    )
                }
            }

            is ChatScreen.State.Loading -> {
                BackHandler {
                    state.eventSink(ChatScreen.Event.NavigateBack)
                }
                Column(modifier = Modifier) {
                    Text(text = "로딩 중")
                }
            }

            is ChatScreen.State.LoadFailed -> {
                BackHandler {
                    state.eventSink(ChatScreen.Event.NavigateBack)
                }
                Column(modifier = Modifier) {
                    Text(text = "실패")
                }
            }
        }
    }
}

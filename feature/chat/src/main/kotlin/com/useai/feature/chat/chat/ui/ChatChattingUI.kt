package com.useai.feature.chat.chat.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.model.chat.Chat
import com.useai.core.model.chat.Question
import com.useai.feature.chat.chat.ChatScreen
import com.useai.feature.chat.chat.ChattingStreamingStatus

@Composable
internal fun ChatChattingUI(
    state: ChatScreen.State.Chatting,
    modifier: Modifier = Modifier
) {

    TODO("Empty 채팅 뷰")
    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .weight(1f)
        ) {
            chatCommonStickyHeader(
                questions = state.questions,
                currentQuestion = state.currentQuestion,
                onQuestionTabChange = { question ->
                    state.eventSink(ChatScreen.Event.ChangeQuestion(question))
                },
                onQuestionAdd = {
                    state.eventSink(ChatScreen.Event.AddQuestion)
                },
                onCategoryChange = { category ->
                    state.eventSink(ChatScreen.Event.ChangeCategory(category))
                }
            )

            items(items = state.chatHistory) { chat ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 35.dp, top = 20.dp)
                ) {
                    when (chat) {
                        is Chat.AI -> {
                            AIChatItem(
                                chat = chat,
                                onUpdateLetterClick = {
                                    state.eventSink(ChatScreen.Event.Chatting.UpdateLetter(chat.message))
                                },
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }

                        is Chat.User -> {
                            UserChatItem(
                                chat = chat,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }

            item {
                if (state.streamingStatus is ChattingStreamingStatus.Streaming) {
                    AIChatItem(
                        chat = state.streamingStatus.chat,
                        onUpdateLetterClick = {},
                    )
                }
            }
        }

        ChatInputRow(
            userInput = state.userInput,
            onUserInputChange = {
                state.eventSink(ChatScreen.Event.InputMessage(it))
            },
            onSendClick = {
                state.eventSink(ChatScreen.Event.Chatting.SendMessage(state.userInput))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatChattingUIPreview() {
    ChatChattingUI(
        state = ChatScreen.State.Chatting(
            chatHistory = listOf(
                Chat.AI.Done(message = "안녕하세요 자소서 대신 써드립니다", isLetter = false),
                Chat.User("으아아아아아ㅏ아아아아아"),
                Chat.AI.Done(message = "그러면 도와드릴 수 없습니다.", isLetter = false),
                Chat.User("써줘"),
                Chat.AI.Done(
                    message = "저는 코딩을 잘하구여 책임감이 뛰어나구요 성실합니다. " +
                            "그리고 초중고를 무사히 졸업했고 4년제 학교를 다녔으며 가리는 거 없이 대부분 잘 먹습니다 ", isLetter = true
                ),
            ), userInput = "제대로 써",
            streamingStatus = ChattingStreamingStatus.Idle,
            questions = listOf(Question("","",1000), Question("","",1000)),
            currentQuestion = Question("","",1000),
            eventSink = {},
        ),
    )
}

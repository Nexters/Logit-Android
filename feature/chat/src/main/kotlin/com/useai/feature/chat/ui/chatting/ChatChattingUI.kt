package com.useai.feature.chat.ui.chatting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.model.chat.ChattingContent
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.Question
import com.useai.feature.chat.ChatScreen
import com.useai.feature.chat.ChatScreenCategory
import com.useai.feature.chat.ChattingStreamingStatus
import com.useai.feature.chat.ui.ChatInputRow
import com.useai.feature.chat.ui.chatCommonStickyHeader
import java.time.LocalDateTime

@Composable
internal fun ChatChattingUI(
    state: ChatScreen.State.Success,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .weight(1f),
        ) {
            chatCommonStickyHeader(
                questions = state.questions,
                currentQuestion = state.currentQuestion,
                currentCategory = ChatScreenCategory.CHATTING,
                isHeaderUIExpanded = state.isHeaderUIExpanded,
                onQuestionTabChange = { question ->
                    state.eventSink(ChatScreen.Event.ChangeQuestion(question))
                },
                onQuestionAdd = {
                    state.eventSink(ChatScreen.Event.AddQuestion)
                },
                onCategoryChange = { category ->
                    state.eventSink(ChatScreen.Event.ChangeCategory(category))
                },
                onQuestionTitleExpand = {
                    state.eventSink(ChatScreen.Event.ExpandOrShrinkHeader)
                }
            )

            items(
                items = state.chattingHistory.chattings,
                key = { chat -> chat.id }
            ) { chat ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 35.dp, top = 20.dp)
                ) {
                    when (chat) {
                        is ChattingContent.AI -> {
                            AIChattingItem(
                                chatting = chat,
                                onUpdateLetterClick = {
                                    state.eventSink(ChatScreen.Event.UpdateLetter(chat.message))
                                },
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }

                        is ChattingContent.User -> {
                            UserChattingItem(
                                chatting = chat,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }

            item {
                if (state.streamingStatus is ChattingStreamingStatus.Streaming) {
                    AIChattingItem(
                        chatting = ChattingContent.AI(
                            id = "",
                            message = state.streamingStatus.data,
                            createdAt = LocalDateTime.MIN,
                            isLetter = false
                        ),
                        onUpdateLetterClick = { /* Do nothing */ },
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        ChatInputRow(
            userInput = state.userInput,
            onUserInputChange = {
                state.eventSink(ChatScreen.Event.InputMessage(it))
            },
            onSendClick = {
                keyboardController?.hide()
                state.eventSink(ChatScreen.Event.SendMessage(state.userInput))
            },
            isSendEnabled = state.userInput.isNotEmpty(),
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
        state = ChatScreen.State.Success(
            chattingHistory = ChattingHistory(
                chattings = listOf(
                    ChattingContent.AI(
                        message = "안녕하세요 자소서 대신 써드립니다",
                        isLetter = false,
                        id = "",
                        createdAt = LocalDateTime.MIN
                    ),
                    ChattingContent.User(
                        message = "으아아아아아ㅏ아아아아아",
                        id = "",
                        createdAt = LocalDateTime.MIN
                    ),
                    ChattingContent.AI(
                        message = "그러면 도와드릴 수 없습니다.",
                        isLetter = false,
                        id = "",
createdAt = LocalDateTime.MIN
                    ),
                    ChattingContent.User(message = "써줘", id = "", createdAt = LocalDateTime.MIN),
                    ChattingContent.AI(
                        message = "저는 코딩을 잘하구여 책임감이 뛰어나구요 성실합니다. " +
                                "그리고 초중고를 무사히 졸업했고 4년제 학교를 다녔으며 가리는 거 없이 대부분 잘 먹습니다 ",
                        isLetter = true,
                        id = "",
                        createdAt = LocalDateTime.MIN
                    ),
                ),
                projectCreatedAt = LocalDateTime.now(),
                experienceIds = emptyList(),
                questionId = "",
                projectName = "Test Project",
                questionTitle = "Test Question",
                nextCursor = null,
                hasMore = false,
                remainingChats = 0
            ),
            userInput = "제대로 써",
            streamingStatus = ChattingStreamingStatus.Idle,
            questions = listOf(Question("", "", 1000, ""), Question("", "", 1000, "")),
            currentQuestion = Question("", "", 1000, ""),
            eventSink = {},
            currentCategory = ChatScreenCategory.CHATTING,
            isHeaderUIExpanded = false
        ),
    )
}

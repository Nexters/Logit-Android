package com.useai.feature.chat.ui.chatting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.slack.circuit.retained.rememberRetained
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.ChattingContent
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.Question
import com.useai.core.model.project.Project
import com.useai.core.ui.experience.ExperienceCard
import com.useai.feature.chat.ChatScreen
import com.useai.feature.chat.ChatScreenCategory
import com.useai.feature.chat.ChattingStreamingStatus
import com.useai.feature.chat.ui.ChatInputRow
import com.useai.feature.chat.ui.chatCommonStickyHeader
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatChattingUI(
    state: ChatScreen.State.Success,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val temporarilySelectedExperiences = rememberRetained {
        mutableStateSetOf(*state.chattingHistory.experienceIds.toTypedArray())
    }

    if (state.showExperienceModal) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            containerColor = LogitTheme.colors.white,
            shape = RoundedCornerShape(14.dp),
            onDismissRequest = {
                state.eventSink(ChatScreen.Event.DismissExperienceModal)
            },
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.padding(top = 27.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_ex_list),
                        tint = LogitTheme.colors.black,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.select_experience),
                        style = LogitTheme.typography.body3_1,
                        color = LogitTheme.colors.black,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.select_experience_explain),
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray300,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 35.dp).clip(RoundedCornerShape(8.dp)).background(
                        shape = RoundedCornerShape(8.dp),
                        color = LogitTheme.colors.primary50
                    ).clickable {
                        TODO("AddExperience Event")
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 14.dp).padding(vertical = 13.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        tint = Color.Unspecified,
                        contentDescription = null,
                    )

                    Text(
                        text = stringResource(R.string.add_experience),
                        style = LogitTheme.typography.body6_2,
                        color = LogitTheme.colors.gray300,
                        modifier = Modifier.padding(start = 8.dp).fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.padding(top = 12.dp))
                state.matchingExperiences.fastForEach { matchingExperience ->
                    ExperienceCard(
                        matchingExperience = matchingExperience,
                        isSelected = matchingExperience.experience.id in temporarilySelectedExperiences,
                        onClick = {
                            if (matchingExperience.experience.id in temporarilySelectedExperiences)
                                temporarilySelectedExperiences.remove(matchingExperience.experience.id)
                            else
                                temporarilySelectedExperiences.add(matchingExperience.experience.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                }

                LogitPrimaryButton(
                    text = stringResource(R.string.select_experiences, temporarilySelectedExperiences.size),
                    onClick = {
                        state.eventSink(ChatScreen.Event.CompleteSelectExperience(
                            experienceIds = temporarilySelectedExperiences.toList()
                        ))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    Column(modifier = modifier) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .weight(1f),
        ) {
            chatCommonStickyHeader(
                projectTitle = state.project.company + " " + state.project.jobPosition,
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
                },
                onBack = {
                    state.eventSink(ChatScreen.Event.NavigateBack)
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
            onTryExperienceUpload = {
                state.eventSink(ChatScreen.Event.TryUploadExperience)
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
                experienceIds = emptySet(),
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
            isHeaderUIExpanded = false,
            project = Project(
                id = "",
                company = "네이버",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                dueDate = LocalDate.now(),
                jobPosition = "안드로이드 개발자",
                recruitNotice = "채용공고",
            ),
            matchingExperiences = listOf(),
        ),
    )
}

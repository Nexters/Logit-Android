package com.useai.feature.chat.ui.letter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.snackbar.LogitSnackbarHost
import com.useai.core.designsystem.component.snackbar.showLogitSnackbar
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.Question
import com.useai.core.model.project.Project
import com.useai.feature.chat.ChatScreen
import com.useai.feature.chat.ChatScreenCategory
import com.useai.feature.chat.ChattingStreamingStatus
import com.useai.feature.chat.ui.chatCommonStickyHeader
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ChatLetterUI(
    state: ChatScreen.State.Success,
    modifier: Modifier = Modifier
) {
    BackHandler {
        state.eventSink(ChatScreen.Event.NavigateBack)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val maxLengthMessage = stringResource(R.string.chat_letter_max_length_message)
    val saveButtonText = stringResource(R.string.chat_letter_save)
    val completeButtonText = stringResource(R.string.chat_letter_complete)
    val maxLength = state.currentQuestion.maxLength

    var letterDraft by remember(state.currentQuestion.id) {
        mutableStateOf(
            TextFieldValue(
                text = state.currentQuestion.letter.take(maxLength),
                selection = TextRange(state.currentQuestion.letter.take(maxLength).length)
            )
        )
    }

    LaunchedEffect(state.currentQuestion.id, state.currentQuestion.letter) {
        val nextText = state.currentQuestion.letter.take(maxLength)
        letterDraft = TextFieldValue(
            text = nextText,
            selection = TextRange(nextText.length)
        )
    }

    val trimmedDraft = letterDraft.text.trimEnd()
    val isLetterChanged = trimmedDraft != state.currentQuestion.letter

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(bottom = 72.dp)
        ) {
            chatCommonStickyHeader(
                projectTitle = state.project.company + " " + state.project.jobPosition,
                questions = state.questions,
                currentQuestion = state.currentQuestion,
                currentCategory = ChatScreenCategory.LETTER,
                isHeaderUIExpanded = state.isHeaderUIExpanded,
                extraStickyContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LogitTheme.colors.white)
                            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Text(
                            style = LogitTheme.typography.body5_5,
                            text = "${letterDraft.text.length} / $maxLength",
                            color = LogitTheme.colors.gray200
                        )
                        Row(
                            modifier = Modifier
                                .height(25.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = if (state.currentQuestion.isCompleted) {
                                        LogitTheme.colors.primary100
                                    } else {
                                        LogitTheme.colors.gray100
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = if (state.currentQuestion.isCompleted) {
                                        LogitTheme.colors.primary50
                                    } else {
                                        LogitTheme.colors.white
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !state.currentQuestion.isCompleted) {
                                    state.eventSink(ChatScreen.Event.CompleteQuestion)
                                }
                                .padding(start = 8.dp, end = 10.dp, top = 4.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    if (state.currentQuestion.isCompleted) {
                                        R.drawable.ic_subtract_blue
                                    } else {
                                        R.drawable.ic_subtract_gray
                                    }
                                ),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(12.dp)
                            )
                            androidx.compose.material3.Text(
                                text = completeButtonText,
                                style = LogitTheme.typography.body9_1,
                                color = if (state.currentQuestion.isCompleted) {
                                    LogitTheme.colors.primary200
                                } else {
                                    LogitTheme.colors.gray200
                                }
                            )
                        }
                    }
                },
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
                onQuestionEdit = {
                    state.eventSink(ChatScreen.Event.EditQuestions)
                },
                onQuestionDelete = {
                    state.eventSink(ChatScreen.Event.ConfirmDeleteProject)
                },
                onBack = {
                    state.eventSink(ChatScreen.Event.NavigateBack)
                }
            )

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp)
                ) {
                    if (letterDraft.text.isBlank()) {
                        androidx.compose.material3.Text(
                            text = stringResource(R.string.chat_letter_placeholder),
                            style = LogitTheme.typography.body7_4,
                            color = LogitTheme.colors.gray200,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    BasicTextField(
                        value = letterDraft,
                        onValueChange = { input ->
                            if (input.text.length > maxLength) {
                                if (snackbarHostState.currentSnackbarData == null) {
                                    scope.launch {
                                        snackbarHostState.showLogitSnackbar(
                                            message = maxLengthMessage,
                                            iconResId = R.drawable.ic_alert
                                        )
                                    }
                                }
                                return@BasicTextField
                            }

                            letterDraft = input
                        },
                        textStyle = LogitTheme.typography.body7_3.copy(color = LogitTheme.colors.gray400),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(LogitTheme.colors.white)
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            LogitPrimaryButton(
                text = saveButtonText,
                enabled = isLetterChanged,
                textStyle = LogitTheme.typography.body3_1,
                onClick = {
                    state.eventSink(ChatScreen.Event.UpdateLetter(trimmedDraft))
                    state.eventSink(ChatScreen.Event.SaveLetter(trimmedDraft))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
            )
        }

        LogitSnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 64.dp)
        )
    }
}

@Composable
@Preview
private fun ChatLetterUIPreview() {
    ChatLetterUI(
        state = ChatScreen.State.Success(
            chattingHistory = ChattingHistory(
                chattings = listOf(),
                projectCreatedAt = LocalDateTime.now(),
                experienceIds = emptySet(),
                questionId = "",
                projectName = "Test Project",
                questionTitle = "Test Question",
                nextCursor = null,
                hasMore = false,
                remainingChats = 0
            ),
            userInput = "",
            streamingStatus = ChattingStreamingStatus.Idle,
            questions = listOf(Question("", "", 1000, "")),
            currentQuestion = Question("", "", 1000, ""),
            eventSink = {},
            currentCategory = ChatScreenCategory.LETTER,
            isHeaderUIExpanded = false,
            project = Project(
                id = "",
                company = "네이버",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                dueDate = LocalDate.now(),
                jobPosition = "프론트엔드 개발",
                recruitNotice = "채용공고",
            ),
            matchingExperiences = listOf()
        ),
    )
}

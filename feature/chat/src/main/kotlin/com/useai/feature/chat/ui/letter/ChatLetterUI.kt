package com.useai.feature.chat.ui.letter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.Question
import com.useai.core.model.project.Project
import com.useai.feature.chat.ChatScreen
import com.useai.feature.chat.ChatScreenCategory
import com.useai.feature.chat.ChattingStreamingStatus
import com.useai.feature.chat.ui.chatCommonStickyHeader
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
internal fun ChatLetterUI(
    state: ChatScreen.State.Success,
    modifier: Modifier = Modifier
) {
    BackHandler {
        state.eventSink(ChatScreen.Event.NavigateBack)
    }

    var letterDraft by remember(state.currentQuestion.id) {
        mutableStateOf(state.currentQuestion.letter)
    }

    LaunchedEffect(state.currentQuestion.id, state.currentQuestion.letter) {
        letterDraft = state.currentQuestion.letter
    }

    var wasFocused by remember { mutableStateOf(false) }

    fun commitLetterIfChanged() {
        val trimmed = letterDraft.trimEnd()
        if (trimmed != state.currentQuestion.letter) {
            state.eventSink(ChatScreen.Event.UpdateLetter(trimmed))
        }
    }

    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            chatCommonStickyHeader(
                projectTitle = state.project.company + " " + state.project.jobPosition,
                questions = state.questions,
                currentQuestion = state.currentQuestion,
                currentCategory = ChatScreenCategory.LETTER,
                isHeaderUIExpanded = state.isHeaderUIExpanded,
                onQuestionTabChange = { question ->
                    commitLetterIfChanged()
                    state.eventSink(ChatScreen.Event.ChangeQuestion(question))
                },
                onQuestionAdd = {
                    commitLetterIfChanged()
                    state.eventSink(ChatScreen.Event.AddQuestion)
                },
                onCategoryChange = { category ->
                    commitLetterIfChanged()
                    state.eventSink(ChatScreen.Event.ChangeCategory(category))
                },
                onQuestionTitleExpand = {
                    state.eventSink(ChatScreen.Event.ExpandOrShrinkHeader)
                },
                onQuestionEdit = {
                    state.eventSink(ChatScreen.Event.EditQuestions)
                },
                onQuestionDelete = {
                    commitLetterIfChanged()
                    state.eventSink(ChatScreen.Event.DeleteProject)
                },
                onBack = {
                    commitLetterIfChanged()
                    state.eventSink(ChatScreen.Event.NavigateBack)
                }
            )

            item {
                androidx.compose.material3.Text(
                    style = LogitTheme.typography.body5_5,
                    text = "${letterDraft.length} / ${state.currentQuestion.maxLength}",
                    color = LogitTheme.colors.gray300,
                    modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp)
                ) {
                    if (letterDraft.isBlank()) {
                        androidx.compose.material3.Text(
                            text = stringResource(R.string.chat_letter_placeholder),
                            style = LogitTheme.typography.body6_1,
                            color = LogitTheme.colors.gray100,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    BasicTextField(
                        value = letterDraft,
                        onValueChange = { input ->
                            letterDraft = input.take(state.currentQuestion.maxLength)
                        },
                        textStyle = LogitTheme.typography.body7_3.copy(color = LogitTheme.colors.gray400),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (wasFocused && !it.isFocused) {
                                    commitLetterIfChanged()
                                }
                                wasFocused = it.isFocused
                            }
                    )
                }
            }
        }
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

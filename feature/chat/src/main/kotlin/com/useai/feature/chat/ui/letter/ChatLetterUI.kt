package com.useai.feature.chat.ui.letter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            chatCommonStickyHeader(
                projectTitle = state.project.company + " " + state.project.jobPosition,
                questions = state.questions,
                currentQuestion = state.currentQuestion,
                currentCategory = ChatScreenCategory.LETTER,
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

            item {
                Text(
                    style = LogitTheme.typography.body5_5,
                    text = stringResource(R.string.length_limit, state.currentQuestion.letter.length, state.currentQuestion.maxLength),
                    color = LogitTheme.colors.gray400
                )
                Spacer(modifier = Modifier.height(9.dp))
            }
            item {
                Text(
                    style = LogitTheme.typography.body7_3,
                    color = LogitTheme.colors.gray400,
                    text = state.currentQuestion.letter,
                    modifier = Modifier.fillMaxWidth().padding(top = 19.dp)
                )
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
                experienceIds = emptyList(),
                questionId = "",
                projectName = "Test Project",
                questionTitle = "Test Question",
                nextCursor = null,
                hasMore = false,
                remainingChats = 0
            ),
            userInput = "",
            streamingStatus = ChattingStreamingStatus.Idle,
            questions = listOf(Question("", "", 1000, "저는 코딩을 잘하구여 책임감이 뛰어나구요 성실합니다. " +
                    "그리고 초중고를 무사히 졸업했고 4년제 학교를 다녔으며 가리는 거 없이 대부분 잘 먹습니다 ")),
            currentQuestion = Question("", "", 1000, "저는 코딩을 잘하구여 책임감이 뛰어나구요 성실합니다. " +
                    "그리고 초중고를 무사히 졸업했고 4년제 학교를 다녔으며 가리는 거 없이 대부분 잘 먹습니다 "),
            eventSink = {},
            currentCategory = ChatScreenCategory.LETTER,
            isHeaderUIExpanded = false,
            project = Project(
                id = "",
                company = "네이버",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                dueDate = LocalDate.now(),
                jobPosition = "안드로이드 개발자",
                recruitNotice = "채용공고"
            ),
            matchingExperiences = listOf()
        ),
    )
}

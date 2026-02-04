package com.useai.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Question
import com.useai.feature.chat.ChatScreenCategory

internal fun LazyListScope.chatCommonStickyHeader(
    questions: List<Question>,
    currentQuestion: Question,
    currentCategory: ChatScreenCategory,
    isHeaderUIExpanded: Boolean,
    onQuestionTabChange: (Question) -> Unit,
    onQuestionAdd: () -> Unit,
    onCategoryChange: (ChatScreenCategory) -> Unit,
    onQuestionTitleExpand: () -> Unit
) {
    stickyHeader {
        QuestionTabRow(
            selectedQuestion = currentQuestion,
            questions = questions,
            onTabSelect = onQuestionTabChange,
            onQuestionAdd = onQuestionAdd,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LogitTheme.colors.white)
        )
        Row(
            modifier = Modifier
                .background(color = LogitTheme.colors.white)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatScreenCategory.entries.fastForEach { category ->
                LogitToggle(
                    isSelected = category == currentCategory,
                    icon = ImageVector.vectorResource(category.icon),
                    text = stringResource(category.title),
                    onSelect = { onCategoryChange(category) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LogitTheme.colors.white)
                .padding(top = 18.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = currentQuestion.title,
                style = LogitTheme.typography.body5_1,
                color = LogitTheme.colors.gray400,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(if (isHeaderUIExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = LogitTheme.colors.gray300,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onQuestionTitleExpand()
                    }.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatCommonStickyHeaderPreview() {
    LazyColumn {
        chatCommonStickyHeader(
            questions = listOf(Question("", "", 1000, ""), Question("", "", 1000, "")),
            currentQuestion = Question("", "[필수] 본 직무에 지원하게 된 동기와 본인이 이 \n" +
                    "포지션에 가장 적합한 후보라고 생각하는 이유를 \n" +
                    "작성해 주세요. |", 1000, ""),
            onQuestionTabChange = {},
            onQuestionAdd = {},
            onCategoryChange = {},
            onQuestionTitleExpand = {},
            isHeaderUIExpanded = false,
            currentCategory = ChatScreenCategory.CHATTING
        )
    }
}

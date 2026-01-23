package com.useai.feature.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.useai.core.model.chat.Question
import com.useai.feature.chat.ChatScreenCategory

internal fun LazyListScope.chatCommonStickyHeader(
    questions: List<Question>,
    currentQuestion: Question,
    onQuestionTabChange: (Question) -> Unit,
    onQuestionAdd: () -> Unit,
    onCategoryChange: (ChatScreenCategory) -> Unit,
) {
    stickyHeader {
        QuestionTabRow(
            selectedQuestion = currentQuestion,
            questions = questions,
            onTabSelect = onQuestionTabChange,
            onQuestionAdd = onQuestionAdd,
            modifier = Modifier.fillMaxWidth()
        )
    }

    stickyHeader {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatScreenCategory.entries.fastForEach { category ->
                LogitToggle(
                    isSelected = category == ChatScreenCategory.CHATTING,
                    icon = ImageVector.vectorResource(category.icon),
                    text = stringResource(category.title),
                    onSelect = { onCategoryChange(category) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatCommonStickyHeaderPreview() {
    LazyColumn {
        chatCommonStickyHeader(
            questions = listOf(Question("", "", 1000, ""), Question("", "", 1000, "")),
            currentQuestion = Question("", "", 1000, ""),
            onQuestionTabChange = {},
            onQuestionAdd = {},
            onCategoryChange = {}
        )
    }
}

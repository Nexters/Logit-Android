package com.useai.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Question

private val questionChipShape = RoundedCornerShape(10.dp)

@Composable
internal fun QuestionTabRow(
    selectedQuestion: Question,
    questions: List<Question>,
    onTabSelect: (Question) -> Unit,
    onQuestionAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTabIndex = questions.indexOf(selectedQuestion)

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
    ) {
        itemsIndexed(questions) { index, question ->
            val isSelected = index == selectedTabIndex
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) LogitTheme.colors.primary20 else LogitTheme.colors.white,
                        shape = questionChipShape
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) LogitTheme.colors.primary100 else LogitTheme.colors.gray100,
                        shape = questionChipShape
                    )
                    .clickable { onTabSelect(question) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.question_number, index + 1),
                    style = if (isSelected) LogitTheme.typography.body6_2 else LogitTheme.typography.body6_1,
                    color = if (isSelected) LogitTheme.colors.primary100 else LogitTheme.colors.gray300,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 32.dp)
                    .background(color = LogitTheme.colors.white, shape = questionChipShape)
                    .border(width = 1.dp, color = LogitTheme.colors.gray100, shape = questionChipShape)
                    .clickable(onClick = onQuestionAdd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_tab_add),
                    contentDescription = null,
                    tint = LogitTheme.colors.gray300,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuestionTabRowPreview() {
    val q = Question(id = "", title = "", maxLength = 1000, letter = "")
    QuestionTabRow(
        selectedQuestion = q,
        onTabSelect = {},
        onQuestionAdd = {},
        questions = buildList {
            repeat(4) {
                add(q)
            }
        },
    )
}

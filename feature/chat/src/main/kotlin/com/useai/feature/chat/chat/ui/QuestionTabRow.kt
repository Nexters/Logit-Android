package com.useai.feature.chat.chat.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Question

@Composable
internal fun QuestionTabRow(
    selectedTabIndex: Int,
    questions: List<Question>,
    onTabSelect: (Question) -> Unit,
    onQuestionAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                width = Dp.Unspecified,
                height = 1.5.dp,
                color = LogitTheme.colors.primary100
            )
        },
        containerColor = LogitTheme.colors.white,
        contentColor = LogitTheme.colors.black,
        edgePadding = 20.dp,
        minTabWidth = 0.dp
    ) {
        questions.fastForEachIndexed { i, question ->
            val isSelected = i == selectedTabIndex
            Tab(
                selected = isSelected,
                onClick = {
                    onTabSelect(question)
                },
                modifier = Modifier.padding(horizontal = 14.dp)
            ) {
                Text(
                    text = stringResource(R.string.question_number, i),
                    style = if (isSelected) LogitTheme.typography.body5_2 else LogitTheme.typography.body5_3,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = if (isSelected) LogitTheme.colors.primary100 else LogitTheme.colors.gray400
                )
            }
        }
        Tab(
            selected = false,
            onClick = onQuestionAdd,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Preview
@Composable
private fun QuestionTabRowPreview(){
    QuestionTabRow(
        selectedTabIndex = 0,
        onTabSelect = {},
        onQuestionAdd = {},
        questions = buildList {
            repeat(3){
                add(Question(title = "", maxLength = 3))
            }
        }
    )
}

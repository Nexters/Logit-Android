package com.useai.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Question
import com.useai.core.ui.LogitDropdownMenu
import com.useai.core.ui.LogitDropdownMenuItem
import com.useai.core.ui.noRippleClickable
import com.useai.feature.chat.ChatScreenCategory

internal fun LazyListScope.chatCommonStickyHeader(
    projectTitle: String,
    questions: List<Question>,
    currentQuestion: Question,
    currentCategory: ChatScreenCategory,
    isHeaderUIExpanded: Boolean,
    extraStickyContent: (@Composable () -> Unit)? = null,
    onQuestionTabChange: (Question) -> Unit,
    onQuestionAdd: () -> Unit,
    onCategoryChange: (ChatScreenCategory) -> Unit,
    onQuestionTitleExpand: () -> Unit,
    onQuestionEdit: () -> Unit,
    onQuestionDelete: () -> Unit,
    onBack: () -> Unit
) {
    stickyHeader {
        var menuExpanded by remember { mutableStateOf(false) }
        var isTitleExpandable by remember(currentQuestion.id, currentQuestion.title) {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LogitTheme.colors.white)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_navigate_left),
                contentDescription = stringResource(R.string.content_description_navigate_back),
                tint = LogitTheme.colors.black,
                modifier = Modifier.noRippleClickable { onBack() }
            )
            Text(
                text = projectTitle,
                style = LogitTheme.typography.body4,
                color = LogitTheme.colors.black,
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Box {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more_vertical),
                    contentDescription = null,
                    tint = LogitTheme.colors.black,
                    modifier = Modifier.noRippleClickable { menuExpanded = true }
                )
                LogitDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    LogitDropdownMenuItem(
                        text = stringResource(R.string.chat_edit),
                        onClick = {
                            menuExpanded = false
                            onQuestionEdit()
                        },
                        icon = ImageVector.vectorResource(R.drawable.ic_write),
                    )
                    LogitDropdownMenuItem(
                        text = stringResource(R.string.chat_delete),
                        onClick = {
                            menuExpanded = false
                            onQuestionDelete()
                        },
                        icon = ImageVector.vectorResource(R.drawable.ic_trash_drop),
                        iconTint = Color.Unspecified,
                    )
                }
            }
        }

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
                .fillMaxWidth()
                .background(color = LogitTheme.colors.white)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = currentQuestion.title,
                style = LogitTheme.typography.body5_1,
                color = LogitTheme.colors.gray400,
                maxLines = if (isHeaderUIExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (!isHeaderUIExpanded) {
                        isTitleExpandable = textLayoutResult.hasVisualOverflow
                    }
                },
                modifier = Modifier.weight(1f)
            )

            if (isHeaderUIExpanded || isTitleExpandable) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (isHeaderUIExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                    ),
                    contentDescription = null,
                    tint = LogitTheme.colors.gray300,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .clickable { onQuestionTitleExpand() }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LogitTheme.colors.white)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val textMeasurer = rememberTextMeasurer()
            val density = LocalDensity.current
            val tabLabelStyle = LogitTheme.typography.body6_2
            val tabLabelWidths = with(density) {
                ChatScreenCategory.entries.associateWith { category ->
                    textMeasurer.measure(
                        text = AnnotatedString(stringResource(category.title)),
                        style = tabLabelStyle
                    ).size.width.toDp()
                }
            }
            val tabHorizontalPadding = 4.dp

            ChatScreenCategory.entries.fastForEach { category ->
                val isSelected = category == currentCategory
                val selectedUnderlineColor = LogitTheme.colors.gray400
                val tabContentWidth = tabLabelWidths.getValue(category) + (tabHorizontalPadding * 2)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onCategoryChange(category) }
                ) {
                    Text(
                        text = stringResource(category.title),
                        style = if (isSelected) LogitTheme.typography.body6_2 else LogitTheme.typography.body6_1,
                        color = if (isSelected) LogitTheme.colors.black else LogitTheme.colors.gray200,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier
                            .width(tabContentWidth)
                            .padding(horizontal = tabHorizontalPadding)
                            .padding(top = 8.dp, bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .width(tabContentWidth)
                            .height(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(tabLabelWidths.getValue(category))
                                .height(2.dp)
                                .background(
                                    if (isSelected) selectedUnderlineColor else Color.Transparent
                                )
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            color = LogitTheme.colors.gray70,
            thickness = 1.dp
        )
        extraStickyContent?.invoke()
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatCommonStickyHeaderPreview() {
    androidx.compose.foundation.lazy.LazyColumn {
        chatCommonStickyHeader(
            projectTitle = "네이버 프론트엔드 개발",
            questions = listOf(
                Question("", "[필수] 본 직무에 지원하게 된 동기와 본인이 포지션에 가장 적합한 이유", 1000, ""),
                Question("", "", 1000, ""),
                Question("", "", 1000, "")
            ),
            currentQuestion = Question("", "[필수] 본 직무에 지원하게 된 동기와 본인이 포지션에 가장 적합한 이유", 1000, ""),
            onQuestionTabChange = {},
            onQuestionAdd = {},
            onCategoryChange = {},
            onQuestionTitleExpand = {},
            onQuestionEdit = {},
            onQuestionDelete = {},
            isHeaderUIExpanded = false,
            currentCategory = ChatScreenCategory.CHATTING,
            onBack = {}
        )
    }
}

package com.useai.feature.experience.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience
import com.useai.core.ui.LogitDropdownMenu
import com.useai.core.ui.LogitDropdownMenuItem
import com.useai.core.ui.experience.CategoryChip
import com.useai.core.ui.experience.TagChip
import com.useai.feature.experience.ExperienceDetailScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate
import java.util.Locale

@Composable
@CircuitInject(ExperienceDetailScreen::class, ActivityRetainedComponent::class)
fun ExperienceDetailUI(
    state: ExperienceDetailScreen.State,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        when (state) {
            ExperienceDetailScreen.State.Loading -> Unit
            is ExperienceDetailScreen.State.LoadFailed -> state.eventSink(ExperienceDetailScreen.Event.Back)
            is ExperienceDetailScreen.State.Success -> state.eventSink(ExperienceDetailScreen.Event.Back)
        }
    }

    when (state) {
        ExperienceDetailScreen.State.Loading -> ExperienceLoadingUI(modifier = modifier)
        is ExperienceDetailScreen.State.LoadFailed -> {
            ExperienceLoadFailedUI(
                onRetry = { state.eventSink(ExperienceDetailScreen.Event.Retry) },
                modifier = modifier
            )
        }

        is ExperienceDetailScreen.State.Success -> {
            ExperienceDetailContent(
                experience = state.experience,
                isMenuExpanded = state.isMenuExpanded,
                isDeleting = state.isDeleting,
                onBack = { state.eventSink(ExperienceDetailScreen.Event.Back) },
                onMore = { state.eventSink(ExperienceDetailScreen.Event.ClickMore) },
                onDismissMenu = { state.eventSink(ExperienceDetailScreen.Event.DismissMenu) },
                onClickEdit = { state.eventSink(ExperienceDetailScreen.Event.ClickEdit) },
                onClickDelete = { state.eventSink(ExperienceDetailScreen.Event.ClickDelete) },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ExperienceDetailContent(
    experience: Experience,
    isMenuExpanded: Boolean,
    isDeleting: Boolean,
    onBack: () -> Unit,
    onMore: () -> Unit,
    onDismissMenu: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 14.dp, bottom = 30.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_navigate_left),
                tint = LogitTheme.colors.gray300,
                contentDescription = stringResource(R.string.content_description_navigate_back),
                modifier = Modifier.clickable(onClick = onBack)
            )

            Spacer(modifier = Modifier.weight(1f))

            androidx.compose.foundation.layout.Box {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more_vertical),
                    tint = LogitTheme.colors.gray300,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(onClick = onMore)
                        .padding(2.dp)
                )
                LogitDropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = onDismissMenu,
                ) {
                    LogitDropdownMenuItem(
                        text = stringResource(R.string.chat_edit),
                        icon = ImageVector.vectorResource(R.drawable.ic_write),
                        onClick = onClickEdit
                    )
                    LogitDropdownMenuItem(
                        text = stringResource(R.string.chat_delete),
                        icon = ImageVector.vectorResource(R.drawable.ic_trash_drop),
                        enabled = !isDeleting,
                        onClick = onClickDelete
                    )
                }
            }
        }

        Text(
            text = experience.title,
            style = LogitTheme.typography.body3_1.copy(fontWeight = FontWeight.Bold),
            color = LogitTheme.colors.black,
            modifier = Modifier.padding(top = 22.dp)
        )

        ExperienceDetailLabel(
            title = stringResource(R.string.experience_detail_type_label),
            value = experience.experienceType,
            modifier = Modifier.padding(top = 32.dp)
        )

        ExperienceDetailPeriodLabel(
            startDate = experience.startDate,
            endDate = experience.endDate,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = stringResource(R.string.experience_detail_keyword_label),
            style = LogitTheme.typography.body7_2,
            color = LogitTheme.colors.gray200,
            modifier = Modifier.padding(top = 24.dp)
        )

        FlowRow(
            modifier = Modifier.padding(top = 8.dp).height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CategoryChip(category = experience.category)
            experience.tags.fastForEach { tag ->
                TagChip(
                    tag = tag,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }

        ExperienceDetailSection(
            title = stringResource(R.string.experience_detail_situation),
            content = experience.situation,
            modifier = Modifier.padding(top = 56.dp)
        )
        ExperienceDetailSection(
            title = stringResource(R.string.experience_detail_task),
            content = experience.task,
            modifier = Modifier.padding(top = 40.dp)
        )
        ExperienceDetailSection(
            title = stringResource(R.string.experience_detail_action),
            content = experience.action,
            modifier = Modifier.padding(top = 40.dp)
        )
        ExperienceDetailSection(
            title = stringResource(R.string.experience_detail_result),
            content = experience.result,
            modifier = Modifier.padding(top = 40.dp)
        )
    }
}

@Composable
private fun ExperienceDetailLabel(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = LogitTheme.typography.body7_2,
            color = LogitTheme.colors.gray200,
        )
        Text(
            text = value,
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.black,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ExperienceDetailPeriodLabel(
    startDate: LocalDate,
    endDate: LocalDate?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.experience_detail_period_label),
            style = LogitTheme.typography.body7_2,
            color = LogitTheme.colors.gray200,
        )
        Text(
            text = buildAnnotatedString {
                append(startDate.toDisplayDate())
                append(" ")
                pushStyle(SpanStyle(color = LogitTheme.colors.gray200))
                append("~")
                pop()
                append(" ")
                append(endDate?.toDisplayDate() ?: stringResource(R.string.experience_detail_period_in_progress))
            },
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.black,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ExperienceDetailSection(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = LogitTheme.typography.body7_2,
            color = LogitTheme.colors.gray200,
        )
        Text(
            text = content,
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.black,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun LocalDate.toDisplayDate(): String = String.format(
    Locale.KOREA,
    "%04d. %02d. %02d",
    year,
    monthValue,
    dayOfMonth
)

@Preview(showBackground = true)
@Composable
private fun ExperienceDetailSuccessPreview() {
    LogitTheme {
        ExperienceDetailUI(
            state = ExperienceDetailScreen.State.Success(
                experience = Experience(
                    id = "preview-id",
                    tags = listOf("소통력", "협력적소통", "협력적소통"),
                    situation = "서비스 초기 단계에서 데이터 기반 개선이 필요했습니다.",
                    task = "핵심 전환 지표를 2주 안에 개선하는 목표를 세웠습니다.",
                    action = "퍼널 분석, A/B 테스트, 카피 개선을 반복하며 우선순위를 조정했습니다.",
                    result = "전환율 35% 개선과 이탈률 감소를 달성했습니다.",
                    category = com.useai.core.model.experience.ExperienceCategory.COLLABORATIVE_COMMUNICATION,
                    startDate = LocalDate.of(2022, 4, 6),
                    endDate = LocalDate.of(2022, 4, 6),
                    experienceType = "인턴",
                    formatType = "STAR",
                    title = "로짓 데이터 분석을 통한 이탈률 개선"
                ),
                isMenuExpanded = false,
                isDeleting = false,
                eventSink = {}
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExperienceDetailLoadingPreview() {
    LogitTheme {
        ExperienceDetailUI(
            state = ExperienceDetailScreen.State.Loading
        )
    }
}

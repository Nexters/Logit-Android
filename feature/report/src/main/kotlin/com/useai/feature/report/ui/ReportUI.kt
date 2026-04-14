package com.useai.feature.report.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.report.ExperienceCategoryCount
import com.useai.core.model.report.ExperienceReportType
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.model.report.ExperienceTagCount
import com.useai.core.model.report.ExperienceTypeCount
import com.useai.core.ui.LogitPageLoadingView
import com.useai.feature.report.ReportScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(ReportScreen::class, ActivityRetainedComponent::class)
fun ReportUI(
    state: ReportScreen.State,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ReportScreen.State.Loading -> {
            ReportLoading(modifier = modifier)
        }

        is ReportScreen.State.LoadFailed -> {
            ReportLoadFailed(
                modifier = modifier,
                onRetry = { state.eventSink(ReportScreen.Event.Retry) }
            )
        }

        is ReportScreen.State.Success -> {
            ReportSuccessUI(
                modifier = modifier,
                userName = state.userName,
                summary = state.summary,
                scrollState = state.scrollState,
                onClickAddExperience = { state.eventSink(ReportScreen.Event.AddExperience) },
            )
        }
    }
}

@Composable
private fun ReportLoading(
    modifier: Modifier = Modifier,
) {
    LogitPageLoadingView(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    )
}

@Composable
private fun ReportLoadFailed(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.report_load_failed),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.gray300,
        )
        LogitPrimaryButton(
            text = stringResource(R.string.report_retry),
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun ReportSuccessUI(
    userName: String,
    summary: ExperienceSummary,
    scrollState: LazyListState,
    onClickAddExperience: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val chartColors = listOf(
        Color(0xFFBFEFEC),
        Color(0xFFC5ECF8),
        Color(0xFFDDE1FF),
        Color(0xFFE4DAF8),
        Color(0xFFEDD8F3),
        Color(0xFFF4D8E9),
    )

    val topCategory = summary.categoryCounts.maxByOrNull { it.count }
    val displayName = userName.ifBlank { stringResource(R.string.report_default_user_name) }
    val hasNoExperience = summary.total <= 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(LogitTheme.colors.gray20),
    ) {
        Text(
            text = stringResource(R.string.report_profile_title_with_user, displayName),
            style = LogitTheme.typography.body1,
            color = LogitTheme.colors.black,
            modifier = Modifier
                .fillMaxWidth()
                .background(LogitTheme.colors.white)
                .padding(horizontal = 20.dp, vertical = 10.dp),
        )

        if (hasNoExperience) {
            ReportEmptyExperienceFrame(
                onClickAddExperience = onClickAddExperience,
                modifier = Modifier.fillMaxSize().background(LogitTheme.colors.white),
            )
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .background(LogitTheme.colors.white)
                        .padding(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    if (topCategory?.category != null) {
                        ReportProfileIntroCard(
                            category = topCategory.category,
                            modifier = Modifier.padding(horizontal = 20.dp),
                        )
                    }
                    ReportTopInsightCard(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = stringResource(R.string.report_top_insight_title_with_user, displayName),
                        description = stringResource(topCategory?.category.reportTopInsightDescriptionResOrDefault()),
                        chips = summary.categoryCounts
                            .sortedByDescending { it.count }
                            .map { it.category },
                    )
                }
            }

            item {
                ReportTypeVerticalChartSection(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    data = summary.categoryCounts.toVerticalChartData(maxSize = 6),
                    colors = chartColors,
                )
            }

            item {
                ReportTagDonutChartSection(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    data = summary.tagCounts.toDonutChartData(maxSize = 6),
                    colors = chartColors,
                )
            }

            item {
                ReportCategoryHorizontalChartSection(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    data = summary.typeCounts.toHorizontalChartData(maxSize = 6),
                    colors = chartColors,
                )
            }
        }
    }
}

@Composable
private fun ReportEmptyExperienceFrame(
    onClickAddExperience: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 72.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_logit_empty),
                tint = Color.Unspecified,
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.experience_empty),
                style = LogitTheme.typography.body5_5,
                color = LogitTheme.colors.gray100,
                modifier = Modifier.padding(top = 12.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            LogitPrimaryButton(
                text = stringResource(R.string.experience_register),
                textStyle = LogitTheme.typography.body5_2,
                onClick = onClickAddExperience,
                modifier = Modifier.width(170.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ReportUIPreview() {
    LogitTheme {
        Scaffold(containerColor = LogitTheme.colors.white) { padding ->
            ReportUI(
                modifier = Modifier.padding(padding),
                state = ReportScreen.State.Success(
                    userName = "사용자",
                    summary = ExperienceSummary(
                        typeCounts = listOf(
                            ExperienceTypeCount(ExperienceReportType.INTERN, 6),
                            ExperienceTypeCount(ExperienceReportType.CLUB, 5),
                            ExperienceTypeCount(ExperienceReportType.FULL_TIME, 2),
                            ExperienceTypeCount(ExperienceReportType.CONTRACT, 1),
                            ExperienceTypeCount(ExperienceReportType.PERSONAL, 1),
                            ExperienceTypeCount(ExperienceReportType.RESEARCH, 1),
                        ),
                        categoryCounts = listOf(
                            ExperienceCategoryCount(ExperienceCategory.TECHNICAL_EXPERTISE, 6),
                            ExperienceCategoryCount(ExperienceCategory.PROACTIVE_EXECUTION, 4),
                            ExperienceCategoryCount(ExperienceCategory.COLLABORATIVE_COMMUNICATION, 3),
                            ExperienceCategoryCount(ExperienceCategory.CREATIVE_PROBLEM_SOLVING, 2),
                            ExperienceCategoryCount(ExperienceCategory.CUSTOMER_VALUE_ORIENTATION, 1),
                            ExperienceCategoryCount(ExperienceCategory.FLEXIBLE_ADAPTABILITY, 1),
                        ),
                        tagCounts = listOf(
                            ExperienceTagCount("데이터분석", 6),
                            ExperienceTagCount("AI/LLM", 4),
                            ExperienceTagCount("백엔드", 4),
                            ExperienceTagCount("API연동", 4),
                            ExperienceTagCount("퍼포먼스개선", 3),
                            ExperienceTagCount("광고집행", 3),
                        ),
                        total = 18,
                    ),
                    scrollState = rememberLazyListState(),
                    eventSink = {},
                ),
            )
        }
    }
}

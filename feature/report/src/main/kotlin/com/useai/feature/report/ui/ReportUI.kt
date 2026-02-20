package com.useai.feature.report.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.useai.core.ui.fullName
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
                summary = state.summary
            )
        }
    }
}

@Composable
private fun ReportLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.report_loading),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.gray300
        )
    }
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
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.report_load_failed),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.gray300
        )
        LogitPrimaryButton(
            text = stringResource(R.string.report_retry),
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun ReportSuccessUI(
    summary: ExperienceSummary,
    modifier: Modifier = Modifier,
) {
    val chartColors = listOf(
        LogitTheme.colors.icon1,
        LogitTheme.colors.icon2,
        LogitTheme.colors.icon4,
        LogitTheme.colors.icon6,
        androidx.compose.ui.graphics.Color(0xFFD39BE8),
        androidx.compose.ui.graphics.Color(0xFFE3B0CF),
    )

    val topCategory = summary.categoryCounts.maxByOrNull { it.count }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.gray20),
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .background(LogitTheme.colors.white)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.report_profile_title),
                    style = LogitTheme.typography.body1,
                    color = LogitTheme.colors.black,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                if(topCategory?.category != null)
                    ReportProfileIntroCard(
                        category = topCategory.category,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                ReportTopInsightCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    title = topCategory?.category?.fullName ?: stringResource(R.string.report_default_top_category),
                    chips = summary.categoryCounts
                        .sortedByDescending { it.count }
                        .map { it.category }
                )
            }
        }

        item {
            ReportTypeVerticalChartSection(
                modifier = Modifier.padding(horizontal = 20.dp),
                data = summary.typeCounts.sortedByDescending { it.count }.take(6),
                colors = chartColors
            )
        }

        item {
            ReportTagDonutChartSection(
                modifier = Modifier.padding(horizontal = 20.dp),
                data = summary.tagCounts.sortedByDescending { it.count }.take(6),
                colors = chartColors
            )
        }

        item {
            ReportCategoryHorizontalChartSection(
                modifier = Modifier.padding(horizontal = 20.dp),
                data = summary.categoryCounts.sortedByDescending { it.count }.take(6),
                colors = chartColors
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
                            ExperienceTagCount("퍼포먼스마케팅", 3),
                            ExperienceTagCount("광고집행", 3),
                        ),
                        total = 18
                    ),
                    eventSink = {}
                )
            )
        }
    }
}

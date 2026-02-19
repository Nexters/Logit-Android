package com.useai.feature.report.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.report.ExperienceReportType
import com.useai.core.ui.experience.CategoryChip

@Composable
internal fun ReportProfileIntroCard(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LogitTheme.colors.primary20)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_symbol_word),
                contentDescription = null,
                modifier = Modifier.height(28.dp)
            )
            Text(
                text = stringResource(R.string.report_intro_text),
                style = LogitTheme.typography.body7_2,
                color = LogitTheme.colors.primary500
            )
        }
    }
}

@Composable
internal fun ReportTopInsightCard(
    title: String,
    chips: List<ExperienceCategory>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LogitTheme.colors.gray20)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.report_insight_title, title),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.black
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            chips.forEach { chip ->
                CategoryChip(category = chip)
            }
        }
    }
}

@Composable
internal fun ReportWhiteSection(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(LogitTheme.colors.white)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        content = content
    )
}

@Composable
internal fun ReportChartLegend(
    labels: List<String>,
    colors: List<Color>,
) {
    val chunks = labels.withIndex().chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        chunks.forEach { chunk ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                chunk.forEach { indexedLabel ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(colors[indexedLabel.index % colors.size])
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = indexedLabel.value,
                            style = LogitTheme.typography.body3_2,
                            color = LogitTheme.colors.primary500,
                            maxLines = 1
                        )
                    }
                }
                repeat(3 - chunk.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

internal fun summaryCountText(total: Int): String = "${total}개"

internal val ExperienceCategory.displayName: String
    get() = when (this) {
        ExperienceCategory.PROACTIVE_EXECUTION -> "주도적 실행력"
        ExperienceCategory.TECHNICAL_EXPERTISE -> "기술적 전문성"
        ExperienceCategory.LOGICAL_ANALYSIS -> "논리적 분석력"
        ExperienceCategory.CREATIVE_PROBLEM_SOLVING -> "창의적 문제해결"
        ExperienceCategory.COLLABORATIVE_COMMUNICATION -> "협력적 소통"
        ExperienceCategory.TENACIOUS_RESPONSIBILITY -> "끈기있는 책임감"
        ExperienceCategory.FLEXIBLE_ADAPTABILITY -> "유연한 적응력"
        ExperienceCategory.CUSTOMER_VALUE_ORIENTATION -> "고객 가치 지향"
    }

internal val ExperienceReportType.displayName: String
    get() = when (this) {
        ExperienceReportType.PART_TIME -> "아르바이트"
        ExperienceReportType.INTERN -> "인턴"
        ExperienceReportType.FULL_TIME -> "정규직"
        ExperienceReportType.CONTRACT -> "계약직"
        ExperienceReportType.VOLUNTEER -> "봉사 활동"
        ExperienceReportType.AWARD -> "수상경력"
        ExperienceReportType.CLUB -> "동아리 활동"
        ExperienceReportType.RESEARCH -> "연구 활동"
        ExperienceReportType.MILITARY -> "군복무"
        ExperienceReportType.PERSONAL -> "개인 활동"
        ExperienceReportType.UNKNOWN -> "기타"
    }

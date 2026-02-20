package com.useai.feature.report.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.util.fastForEach
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.ui.experience.CategoryChip
import com.useai.core.ui.headerRes

@Composable
internal fun ReportProfileIntroCard(
    category: ExperienceCategory,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(category.headerRes),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .aspectRatio(320f / 335f)
    )
}

@Composable
internal fun ReportTopInsightCard(
    title: String,
    chips: List<ExperienceCategory>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.report_insight_title, title),
            style = LogitTheme.typography.body3_1,
            color = LogitTheme.colors.black
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            chips.fastForEach { chip ->
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
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = indexedLabel.value,
                            style = LogitTheme.typography.body7_4,
                            color = Color(0xFF4F5967),
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

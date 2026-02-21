package com.useai.feature.report.ui

import android.graphics.Paint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.report.ExperienceCategoryCount
import com.useai.core.model.report.ExperienceTagCount
import com.useai.core.model.report.ExperienceTypeCount
import com.useai.core.ui.description
import com.useai.core.ui.displayName
import com.useai.core.ui.fullName
import com.useai.core.ui.simpleName
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
internal fun ReportTypeVerticalChartSection(
    data: List<ExperienceCategoryCount>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
) {
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { startAnimation = true }
    val progress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "typeBarProgress"
    )

    val maxItem = data.maxByOrNull { it.count }
    val minItem = data.minByOrNull { it.count }
    val max = data.maxOfOrNull { it.count }?.takeIf { it > 0 } ?: 1

    ReportWhiteSection(modifier = modifier) {
        Text(
            text = "${maxItem?.category?.simpleName ?: "-"}이 두드러져요",
            style = LogitTheme.typography.body3_1,
            color = LogitTheme.colors.black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "${minItem?.category?.simpleName ?: "-"}을 보완하면 더 균형 잡힌 역량의 인재로 보일 수 있어요!",
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.gray300
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { index, item ->
                val color = colors[index % colors.size]
                val barRatio = item.count.toFloat() / max.toFloat()
                val animatedRatio = barRatio * progress
                val animatedCount = (item.count * progress).roundToInt()

                Column(
                    modifier = Modifier.width(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = animatedCount.toString(),
                        style = LogitTheme.typography.body9_2,
                        color = color
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(((130f * animatedRatio) + 18f).dp)
                            .clip(RoundedCornerShape(9.dp))
                            .background(color.copy(alpha = 0.55f))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        ReportChartLegend(labels = data.map { it.category.simpleName }, colors = colors)
    }
}

@Composable
internal fun ReportTagDonutChartSection(
    data: List<ExperienceTagCount>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
) {
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { startAnimation = true }
    val progress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "donutProgress"
    )

    val maxItem = data.maxByOrNull { it.count }
    val total = data.sumOf { it.count }.takeIf { it > 0 } ?: 1

    ReportWhiteSection(modifier = modifier) {
        Text(
            text = "${maxItem?.tag ?: "-"}에 강점이 있어요",
            style = LogitTheme.typography.body3_1,
            color = LogitTheme.colors.black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "{각 해시태그 별 전문성을 강조하는 지정 멘트}",
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.gray300
        )
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val stroke = 26.dp.toPx()
                val diameter = min(size.width, size.height)
                val arcRadius = (diameter / 2f) - (stroke / 2f)
                val labelRadius = arcRadius + (stroke * 0.5f)
                val center = Offset(size.width / 2f, size.height / 2f)
                val visualGap = 0f
                var startAngle = -90f
                var remainingSweep = 360f * progress
                val capAngle = ((stroke / 2f) / arcRadius) * (180f / PI.toFloat())
                val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                val textPaint = Paint().apply {
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                    textSize = 14.dp.toPx()
                    typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                }

                data.forEachIndexed { index, item ->
                    val segmentSweep = item.count.toFloat() / total.toFloat() * 360f
                    val visibleSegmentSweep = min(segmentSweep, remainingSweep).coerceAtLeast(0f)
                    val drawSweep = (visibleSegmentSweep - visualGap - (2f * capAngle)).coerceAtLeast(0f)
                    val drawStart = startAngle + (visualGap / 2f) + capAngle
                    val color = colors[index % colors.size]

                    if (drawSweep > 0f) {
                        drawArc(
                            color = color.copy(alpha = 0.55f),
                            startAngle = drawStart,
                            sweepAngle = drawSweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    if (drawSweep >= 10f) {
                        val midAngle = drawStart + drawSweep / 2f
                        val rad = Math.toRadians(midAngle.toDouble())
                        val textX = center.x + (labelRadius * cos(rad)).toFloat()
                        val textCenterY = center.y + (labelRadius * sin(rad)).toFloat()
                        val textBaselineY = textCenterY - ((textPaint.ascent() + textPaint.descent()) / 2f)
                        textPaint.color = color.toArgb()
                        drawContext.canvas.nativeCanvas.drawText(item.count.toString(), textX, textBaselineY, textPaint)
                    }

                    remainingSweep = (remainingSweep - segmentSweep).coerceAtLeast(0f)
                    startAngle += segmentSweep
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "경험키워드",
                    style = LogitTheme.typography.body5_2,
                    color = LogitTheme.colors.primary500
                )
                Text(
                    text = "${summaryCountText(total)} 집계",
                    style = LogitTheme.typography.body9_2,
                    color = LogitTheme.colors.gray200
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        ReportChartLegend(labels = data.map { it.tag }, colors = colors)
    }
}

@Composable
internal fun ReportCategoryHorizontalChartSection(
    data: List<ExperienceTypeCount>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
) {
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { startAnimation = true }
    val progress by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "categoryBarProgress"
    )

    val maxItem = data.maxByOrNull { it.count }
    val max = data.maxOfOrNull { it.count }?.takeIf { it > 0 } ?: 1

    ReportWhiteSection(modifier = modifier) {
        Text(
            text = "${maxItem?.type?.displayName ?: "-"}이 가장 많아요",
            style = LogitTheme.typography.body3_1,
            color = LogitTheme.colors.black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "${maxItem?.type?.description}",
            style = LogitTheme.typography.body6_1,
            color = LogitTheme.colors.gray300
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            data.fastForEachIndexed { index, item ->
                val color = colors[index % colors.size]
                val ratio = item.count.toFloat() / max.toFloat()
                val animatedRatio = ratio * progress
                val animatedCount = (item.count * progress).roundToInt()

                Row(
                    modifier = Modifier.fillMaxWidth(.8f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = animatedCount.toString(),
                        modifier = Modifier.width(25.dp),
                        textAlign = TextAlign.End,
                        style = LogitTheme.typography.body9_2,
                        color = color
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(LogitTheme.colors.gray70)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedRatio)
                                .height(22.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(color.copy(alpha = 0.55f))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ReportChartLegend(labels = data.map { it.type.displayName }, colors = colors)
    }
}

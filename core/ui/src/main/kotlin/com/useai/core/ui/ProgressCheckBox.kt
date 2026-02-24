package com.useai.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun ProgressCheckBox(
    checked: Boolean,
    text: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LogitTheme.typography.body7_3,
) {
    val checkMarkColor = LogitTheme.colors.white
    val checkDrawProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "progress_check_draw"
    )

    Row(
        modifier = modifier.noRippleClickable { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = if (checked) LogitTheme.colors.primary100 else LogitTheme.colors.gray100,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = if (checked) LogitTheme.colors.primary100 else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            if (checkDrawProgress > 0f) {
                Canvas(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(16.dp)
                ) {
                    val firstStart = Offset(x = size.width * 0.12f, y = size.height * 0.52f)
                    val firstEnd = Offset(x = size.width * 0.42f, y = size.height * 0.82f)
                    val secondStart = Offset(x = size.width * 0.40f, y = size.height * 0.82f)
                    val secondEnd = Offset(x = size.width * 0.88f, y = size.height * 0.22f)

                    val firstSegmentProgress = (checkDrawProgress * 2f).coerceIn(0f, 1f)
                    val secondSegmentProgress = ((checkDrawProgress - 0.5f) * 2f).coerceIn(0f, 1f)

                    drawLine(
                        color = checkMarkColor,
                        start = firstStart,
                        end = lerp(firstStart, firstEnd, firstSegmentProgress),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    if (secondSegmentProgress > 0f) {
                        drawLine(
                            color = checkMarkColor,
                            start = secondStart,
                            end = lerp(secondStart, secondEnd, secondSegmentProgress),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        Text(
            text = text,
            color = LogitTheme.colors.black,
            style = textStyle,
        )
    }
}

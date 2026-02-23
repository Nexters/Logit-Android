package com.useai.feature.experience.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.noRippleClickable

@Composable
internal fun ExperienceCreateSectionHeader(
    title: String,
    description: String,
    onClickLoadExample: (() -> Unit)?,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = title,
                    style = LogitTheme.typography.body3_1,
                    color = LogitTheme.colors.black
                )
                Text(
                    text = description,
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray300,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        if (onClickLoadExample != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = LogitTheme.colors.gray20,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = LogitTheme.colors.gray70,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .noRippleClickable(onClick = onClickLoadExample)
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.experience_create_load_example),
                    style = LogitTheme.typography.body9_3,
                    color = LogitTheme.colors.primary400
                )
            }
        }
    }
}

@Composable
internal fun ExperienceSelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (selected) LogitTheme.colors.primary50 else LogitTheme.colors.gray50,
                shape = RoundedCornerShape(8.dp)
            )
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                tint = if (selected) Color.Unspecified else LogitTheme.colors.gray100,
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(6.dp))
        }
        Text(
            text = text,
            style = LogitTheme.typography.body6_1,
            color = if (selected) LogitTheme.colors.primary200 else LogitTheme.colors.gray300
        )
    }
}

@Composable
internal fun ProgressCheckBox(
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkMarkColor = LogitTheme.colors.white
    val checkDrawProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "progress_check_draw"
    )

    Row(
        modifier = modifier.noRippleClickable(onClick = onClick),
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
            text = "진행 중",
            style = LogitTheme.typography.body7_3,
            color = LogitTheme.colors.black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

package com.useai.feature.experience.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

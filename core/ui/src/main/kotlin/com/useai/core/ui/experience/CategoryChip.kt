package com.useai.core.ui.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.ui.backgroundColor
import com.useai.core.ui.iconRes
import com.useai.core.ui.simpleName

@Composable
fun CategoryChip(
    category: ExperienceCategory,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.background(
            color = category.backgroundColor,
            shape = RoundedCornerShape(8.dp)
        ).padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(category.iconRes),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Text(
            text = category.simpleName,
            style = LogitTheme.typography.body9_3,
            color = LogitTheme.colors.primary600,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
@Preview
private fun CategoryChipPreview() {
    CategoryChip(
        category = ExperienceCategory.FLEXIBLE_ADAPTABILITY,
    )
}

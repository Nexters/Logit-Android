package com.useai.core.ui.experience

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.MatchingExperience

@Composable
fun ExperienceCard(
    experience: Experience,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        Text(
            text = experience.title,
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.primary500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 12.dp)
        )

        FlowRow(
            modifier = Modifier.padding(top = 10.dp).height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CategoryChip(
                category = experience.category,
            )

            experience.tags.forEach { tag ->
                TagChip(
                    tag = tag,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun ExperienceCard(
    matchingExperience: MatchingExperience,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp, horizontal = 18.dp),
    shape: Shape = RoundedCornerShape(14.dp),
    border: BorderStroke = if (isSelected) BorderStroke(
        width = 1.dp,
        color = LogitTheme.colors.primary100
    ) else BorderStroke(width = 1.dp, color = LogitTheme.colors.gray70)
) {

    Column(modifier = modifier
        .clip(shape)
        .border(border, shape)
        .clickable {
            onClick()
        }
        .padding(contentPadding)
    ) {

        Text(
            text = stringResource(R.string.experience_score, (matchingExperience.score * 100).toInt()),
            style = LogitTheme.typography.body8_1,
            color = LogitTheme.colors.primary200
        )

        Text(
            text = matchingExperience.experience.title,
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.primary500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 12.dp)
        )

        FlowRow(
            modifier = Modifier.padding(top = 10.dp).height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CategoryChip(
                category = matchingExperience.experience.category,
            )

            matchingExperience.experience.tags.forEach { tag ->
                TagChip(
                    tag = tag,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

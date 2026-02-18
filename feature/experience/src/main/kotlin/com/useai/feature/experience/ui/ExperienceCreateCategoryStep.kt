package com.useai.feature.experience.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.useai.core.designsystem.R
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.ui.iconRes
import com.useai.core.ui.simpleName
import com.useai.feature.experience.ExperienceCreateScreen

@Composable
internal fun ExperienceCreateCategoryStep(
    state: ExperienceCreateScreen.State,
) {
    Column {
        ExperienceCreateSectionHeader(
            title = stringResource(R.string.experience_create_step3_title),
            description = stringResource(R.string.experience_create_step3_desc),
            onClickLoadExample = null
        )

        Spacer(modifier = Modifier.padding(top = 84.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExperienceCategory.entries.fastForEach { category ->
                ExperienceSelectableChip(
                    text = category.simpleName,
                    selected = state.selectedCategory == category,
                    onClick = { state.eventSink(ExperienceCreateScreen.Event.SelectCategory(category)) },
                    icon = ImageVector.vectorResource(category.iconRes)
                )
            }
        }
    }
}

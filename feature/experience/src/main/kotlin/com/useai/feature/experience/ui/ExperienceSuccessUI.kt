package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience

@Composable
internal fun ExperienceSuccessUI(
    experiences: List<Experience>,
    onClickAdd: () -> Unit,
    onClickRegister: () -> Unit,
    onClickExperienceCard: (String) -> Unit,
    onClickExperienceMore: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.gray20)
    ) {
        ExperienceListHeader(
            count = experiences.size,
            onClickAdd = onClickAdd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        )

        if (experiences.isEmpty()) {
            ExperienceEmptyUI(
                onClickRegister = onClickRegister,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 110.dp, start = 20.dp, end = 20.dp)
            ) {
                items(
                    items = experiences,
                    key = { experience -> experience.id }
                ) { experience ->
                    ExperienceCardListItem(
                        experience = experience,
                        onClickCard = { onClickExperienceCard(experience.id) },
                        onClickMore = { onClickExperienceMore(experience.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

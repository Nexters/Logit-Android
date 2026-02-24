package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience
import com.useai.core.ui.LogitDropdownMenu
import com.useai.core.ui.LogitDropdownMenuItem
import com.useai.core.ui.experience.ExperienceCard

@Composable
internal fun ExperienceCardListItem(
    experience: Experience,
    isMenuExpanded: Boolean,
    isDeleting: Boolean,
    onClickCard: () -> Unit,
    onClickMore: () -> Unit,
    onDismissMenu: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                color = LogitTheme.colors.white,
                shape = RoundedCornerShape(14.dp),
            )
            .border(
                width = 1.dp,
                color = LogitTheme.colors.gray70,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClickCard)
            .padding(start = 18.dp, end = 12.dp, bottom = 14.dp),
    ) {
        ExperienceCard(
            experience = experience,
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_more_vertical),
                tint = LogitTheme.colors.gray300,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onClickMore)
                    .padding(4.dp),
            )

            LogitDropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = onDismissMenu,
            ) {
                LogitDropdownMenuItem(
                    text = stringResource(R.string.chat_edit),
                    icon = ImageVector.vectorResource(R.drawable.ic_write),
                    onClick = onClickEdit,
                )
                LogitDropdownMenuItem(
                    text = stringResource(R.string.chat_delete),
                    icon = ImageVector.vectorResource(R.drawable.ic_trash_drop),
                    enabled = !isDeleting,
                    onClick = onClickDelete,
                )
            }
        }
    }
}

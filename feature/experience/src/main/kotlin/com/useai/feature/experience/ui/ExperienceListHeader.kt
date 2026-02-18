package com.useai.feature.experience.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.noRippleClickable

@Composable
internal fun ExperienceListHeader(
    count: Int,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.experience_list_title),
                style = LogitTheme.typography.body1,
                color = LogitTheme.colors.black,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_tab_add),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .noRippleClickable(onClick = onClickAdd)
                    .padding(8.dp)
            )
        }

        Text(
            text = stringResource(R.string.experience_count, count),
            style = LogitTheme.typography.body8_1,
            color = LogitTheme.colors.gray100,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

package com.useai.feature.newproject.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.noRippleClickable

@Composable
internal fun NewProjectSectionHeader(
    title: String,
    desc: String,
    onClickLoadExample: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Column {
                Text(
                    text = title,
                    style = LogitTheme.typography.body3_1,
                    color = LogitTheme.colors.black
                )
                Text(
                    text = desc,
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray300,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 1.dp)
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
                text = androidx.compose.ui.res.stringResource(R.string.project_form_load_example),
                style = LogitTheme.typography.body9_3,
                color = LogitTheme.colors.primary400.copy(alpha = 0.8f)
            )
        }
    }
}


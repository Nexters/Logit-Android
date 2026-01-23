package com.useai.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

private val Shape = RoundedCornerShape(8.dp)

@Composable
internal fun LogitToggle(
    isSelected: Boolean,
    text: String,
    icon: ImageVector,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .clip(Shape)
            .background(
                color = if (isSelected) LogitTheme.colors.gray400 else LogitTheme.colors.gray50,
                shape = Shape
            ).clickable {
                onSelect()
            }
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .then(modifier)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) LogitTheme.colors.white else LogitTheme.colors.gray100
        )
        Text(
            text = text,
            style = LogitTheme.typography.body6_2,
            color = if (isSelected) LogitTheme.colors.white else LogitTheme.colors.gray200,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
internal fun LogitToggle(
    isSelected: Boolean,
    text: String,
    icon: Painter,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .semantics(mergeDescendants = true) {}
            .clip(Shape)
            .background(
                color = if (isSelected) LogitTheme.colors.gray400 else LogitTheme.colors.gray50,
                shape = Shape
            ).clickable {
                onSelect()
            }
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .then(modifier)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = if (isSelected) LogitTheme.colors.white else LogitTheme.colors.gray100
        )
        Text(
            text = text,
            style = LogitTheme.typography.body6_2,
            color = if (isSelected) LogitTheme.colors.white else LogitTheme.colors.gray200,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
@Preview
private fun LogitToggleSelectedPreview() {
    LogitToggle(
        isSelected = true,
        text = "채팅",
        icon = ImageVector.vectorResource(R.drawable.ic_send),
        onSelect = {}
    )
}


@Composable
@Preview
private fun LogitToggleUnselectedPreview() {
    LogitToggle(
        isSelected = false,
        text = "채팅",
        icon = ImageVector.vectorResource(R.drawable.ic_send),
        onSelect = {}
    )
}

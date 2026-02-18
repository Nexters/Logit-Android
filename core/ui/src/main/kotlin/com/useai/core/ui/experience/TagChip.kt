package com.useai.core.ui.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun TagChip(
    tag: String,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.background(
            color = LogitTheme.colors.gray20,
            shape = RoundedCornerShape(8.dp)
        ).padding(vertical = 5.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            style = LogitTheme.typography.body9_3,
            color = LogitTheme.colors.gray300,
        )
    }
}

@Composable
@Preview
private fun TagChipPreview() {
    TagChip(
        tag = "AI사용능력",
    )
}

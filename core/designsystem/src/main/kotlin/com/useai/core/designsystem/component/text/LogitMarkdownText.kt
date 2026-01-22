package com.useai.core.designsystem.component.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.useai.core.designsystem.theme.LogitTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun LogitMarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    isTextSelectable: Boolean = true,
    style: TextStyle = LogitTheme.typography.body7_3.copy(color = LogitTheme.colors.gray400),
) {
    MarkdownText(
        markdown = markdown,
        modifier = modifier,
        maxLines = maxLines,
        isTextSelectable = isTextSelectable,
        style = style
    )
}

package com.useai.feature.chat.ui.chatting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.component.text.LogitMarkdownText
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Chatting

private val shape by lazy {
    RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 2.dp,
        bottomEnd = 12.dp,
        bottomStart = 12.dp
    )
}

@Composable
internal fun UserChattingItem(
    chatting: Chatting,
    modifier: Modifier = Modifier
) {

    val backgroundBubbleColor = LogitTheme.colors.gray50    // TODO 색상 최신화

    Column(
        modifier = modifier
    ) {
        LogitMarkdownText(
            markdown = chatting.message,
            modifier = Modifier.drawWithCache {
                val outline = shape.createOutline(size, layoutDirection, this)

                onDrawBehind {
                    drawOutline(
                        outline = outline,
                        color = backgroundBubbleColor
                    )
                }
            }.padding(vertical = 8.dp, horizontal = 12.dp)
        )
    }
}

@Preview
@Composable
private fun UserChattingItemPreview(){
    UserChattingItem(
        chatting = Chatting.User(
            message = "디자인 시스템은 아니지만, 동아리에서 공통 로고 \n" +
                "가이드를 만든 적은 있는데 이것도 괜찮을까?",
        ),
        modifier = Modifier
    )
}

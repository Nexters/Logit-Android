package com.useai.feature.chat.chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.text.LogitMarkdownText
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Chat

@Composable
internal fun AIChatItem(
    chat: Chat.AI,
    onUpdateLetterClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_ai_word),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        LogitMarkdownText(
            markdown = chat.message,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (chat is Chat.AI.Done && chat.isLetter) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onUpdateLetterClick() }
                    .padding(vertical = 12.dp, horizontal = 3.dp)
                    .semantics(mergeDescendants = true) {},
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_autorenew),
                    contentDescription = null,
                    tint = LogitTheme.colors.primary400
                )
                Text(
                    text = stringResource(R.string.update_letter),
                    modifier = Modifier.padding(start = 6.dp),
                    style = LogitTheme.typography.body8_1,
                    color = LogitTheme.colors.primary600
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AIChatItemPreview(){
    AIChatItem(
        chat = Chat.AI.Done(
            message = "나랏말싸미 듕귁에 달아 문자와로 " +
                "서르 사맛디 아니할쎄 이런 전차로 어린" +
                " 백셩이 니르고져 홀 배 이셔도 마참내 제 뜨들" +
                " 시러 펴디 몯핧 노미 하니라 내 이랄 위핧야 " +
                "어엿비 너겨 새로 스믈여딻 자랄 맹가노니" +
                " 사람마다 히여 수비 니겨 날로 쓰메 편안킈" +
                " 하고져 핧 따라미니라",
            isLetter = true
        ),
        onUpdateLetterClick = {},
        modifier = Modifier
    )
}

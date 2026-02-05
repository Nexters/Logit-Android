package com.useai.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
internal fun ChatInputRow(
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSendEnabled: Boolean = false
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_ex_list),
            contentDescription = stringResource(R.string.content_description_external),
            tint = Color.Unspecified,
            modifier = Modifier
                .semantics {
                    role = Role.Button
                }
                .fillMaxHeight()
                .clip(CircleShape)
                .clickable {
                    // TODO : 경험 업로드
                }
                .background(
                    color = LogitTheme.colors.primary50,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = LogitTheme.colors.primary70,
                    shape = CircleShape
                )
                .padding(10.dp)
                .aspectRatio(1f)
        )

        ChatInputTextField(
            value = userInput,
            onValueChange = onUserInputChange,
            onSendClick = onSendClick,
            isSendEnabled = isSendEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp)
                .background(color = LogitTheme.colors.white)
                .border(
                    width = 1.dp,
                    color = LogitTheme.colors.gray100,
                    shape = CircleShape
                )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatInputRowPreview() {
    ChatInputRow(
        userInput = "나는 밥을 잘 먹는다는 내용으로 초안 작성해줘",
        onUserInputChange = {},
        onSendClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}

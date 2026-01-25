package com.useai.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
internal fun ChatInputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = LogitTheme.typography.body6_1.copy(
            color = LogitTheme.colors.black
        ),
        singleLine = true
    ) { field ->

        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .padding(start = 14.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value.isEmpty()) {
                Text(
                    text = stringResource(R.string.placeholder_message_input),
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray100
                )
            }
            field()

            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_send),
                contentDescription = stringResource(R.string.content_description_send),
                tint = if (value.isEmpty()) LogitTheme.colors.gray100 else LogitTheme.colors.white,
                modifier = Modifier
                    .semantics {
                        role = Role.Button
                    }
                    .clip(CircleShape)
                    .clickable {
                        onSendClick()
                    }
                    .background(
                        shape = CircleShape,
                        color = if (value.isEmpty()) LogitTheme.colors.gray50 else LogitTheme.colors.primary100
                    )
                    .padding(7.dp)
            )
        }
    }
}

@Composable
@Preview
private fun ChatInputTextFieldPreview() {
    ChatInputTextField(
        value = "자소서 쑤줘",
        onValueChange = {},
        onSendClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = LogitTheme.colors.white,
            )
            .border(
                width = 1.dp,
                color = LogitTheme.colors.gray100,
                shape = CircleShape
            )
    )
}

@Composable
@Preview
private fun ChatInputTextFieldEmptyPreview() {
    ChatInputTextField(
        value = "",
        onValueChange = {},
        onSendClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = LogitTheme.colors.white,
            )
            .border(
                width = 1.dp,
                color = LogitTheme.colors.gray100,
                shape = CircleShape
            )
    )
}

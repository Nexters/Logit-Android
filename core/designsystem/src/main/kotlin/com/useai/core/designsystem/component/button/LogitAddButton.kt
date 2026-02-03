package com.useai.core.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitAddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(14.dp),
        colors = ButtonColors(
            containerColor = LogitTheme.colors.primary50,
            contentColor = LogitTheme.colors.gray300,
            disabledContainerColor = LogitTheme.colors.primary50,
            disabledContentColor = LogitTheme.colors.gray300
        )
    ) {
        Image(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(LogitIcons.Add),
            contentDescription = null,
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = "추가하기",
            style = LogitTheme.typography.body6_2,
        )
    }
}

@Preview
@Composable
private fun LogitAddButtonPreview() {
    LogitAddButton(
        modifier = Modifier.fillMaxWidth()
    )
}

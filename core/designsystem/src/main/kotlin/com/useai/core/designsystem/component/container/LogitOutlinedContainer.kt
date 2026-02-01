package com.useai.core.designsystem.component.container

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitOutlinedContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = LogitTheme.colors.white,
    border: BorderStroke = BorderStroke(1.dp, LogitTheme.colors.gray100),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 6.dp, vertical = 10.dp),
        shape = shape,
        color = containerColor,
        border = border
    ) {
        content()
    }
}

@Preview
@Composable
private fun LogitOutlinedContainerPreview() {
    LogitTheme {
        Box(
            modifier = Modifier
                .background(LogitTheme.colors.white)
        ) {
            LogitOutlinedContainer(
                modifier = Modifier.width(82.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        modifier = Modifier.weight(1f),
                        value = "",
                        onValueChange = {},
                        textStyle = LogitTheme.typography.body6_1,
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                Text(
                                    text = "글자수",
                                    style = LogitTheme.typography.body6_1,
                                    color = LogitTheme.colors.gray200,
                                )
                                innerTextField()
                            }
                        },
                    )
                    Text(
                        text = "자",
                        style = LogitTheme.typography.body6_1,
                        color = LogitTheme.colors.primary500,
                    )
                }
            }
        }
    }
}

package com.useai.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme

object LogitButtonDefaults {

    val Shape = RoundedCornerShape(14.dp)
    val ContentPadding = PaddingValues(vertical = 9.5.dp, horizontal = 37.dp)
    val TextStyle @Composable get() = LogitTheme.typography.body3_2

    @Composable
    fun buttonColors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = LogitTheme.colors.gray100,
        disabledContentColor: Color = LogitTheme.colors.white,
    ): ButtonColors = ButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}

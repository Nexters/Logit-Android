package com.useai.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal val LogitLightColor = LogitColor(
    primary20 = Color(0xFFF5FAFF),
    primary50 = Color(0xFFE5F0FF),
    primary70 = Color(0xFF8DC9FF),
    primary100 = Color(0xFF40A5FF),
    primary200 = Color(0xFF2571EB),
    primary300 = Color(0xFF240991),
    primary400 = Color(0xFF6B7684),
    primary500 = Color(0xFF374151),
    primary600 = Color(0xFF333D4B),
    secondary100 = Color(0xFF4BC0FA),

    white = Color(0xFFFFFFFF),
    gray20 = Color(0xFFF7F9FC),
    gray50 = Color(0xFFF0F1F5),
    gray70 = Color(0xFFE5E7ED),
    gray100 = Color(0xFFBEC2D1),
    gray200 = Color(0xFF828699),
    gray300 = Color(0xFF6B6F84),
    gray400 = Color(0xFF262626),
    black = Color(0xFF17181E),

    icon1 = Color(0xFF63DBD5),
    icon2 = Color(0xFF71D1F0),
    icon3 = Color(0xFF32B1FF),
    icon4 = Color(0xFF8B9AFF),
    icon5 = Color(0xFF8B83E6),
    icon6 = Color(0xFFA283E6),
    icon7 = Color(0xFFCF83E6),
    icon8 = Color(0xFFE683BE),

    alert = Color(0xFFED1728),

    logoGradient = Brush.linearGradient(colors = listOf(Color(0xFF58AEF4), Color(0xFF65C1ED))),
    emptyGradient100 = Brush.linearGradient(colors = listOf(Color(0xFFDEE4FF), Color(0xFFDCF5F4))),
    emptyGradient200 = Brush.linearGradient(colors = listOf(Color(0xFFD4DEFE), Color(0xFFD8F1F7)))
)

data class LogitColor(
    val primary20: Color,
    val primary50: Color,
    val primary70: Color,
    val primary100: Color,
    val primary200: Color,
    val primary300: Color,
    val primary400: Color,
    val primary500: Color,
    val primary600: Color,
    val secondary100: Color,

    val white: Color,
    val gray20: Color,
    val gray50: Color,
    val gray70: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val black: Color,

    val icon1: Color,
    val icon2: Color,
    val icon3: Color,
    val icon4: Color,
    val icon5: Color,
    val icon6: Color,
    val icon7: Color,
    val icon8: Color,

    val alert: Color,

    val logoGradient: Brush,
    val emptyGradient100: Brush,
    val emptyGradient200: Brush,
)

val LocalLogitColor = staticCompositionLocalOf {
    LogitLightColor
}

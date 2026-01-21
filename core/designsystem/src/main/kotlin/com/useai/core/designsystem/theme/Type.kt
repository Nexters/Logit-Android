package com.useai.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.useai.core.designsystem.R

private val Pretendard = FontFamily(
    Font(resId = R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(resId = R.font.pretendard_medium, weight = FontWeight.Medium),
    Font(resId = R.font.pretendard_regular, weight = FontWeight.Normal),
    Font(resId = R.font.pretendard_semibold, weight = FontWeight.SemiBold),
)

data class LogitTypography(
    val headline1: TextStyle,
    val title1: TextStyle,
    val title2: TextStyle,
    val title2_2: TextStyle,
    val title3: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3_1: TextStyle,
    val body3_2: TextStyle,
    val body3_3: TextStyle,
    val body4: TextStyle,
    val body5_1: TextStyle,
    val body5_2: TextStyle,
    val body5_3: TextStyle,
    val body5_4: TextStyle,
    val body5_5: TextStyle,
    val body6_1: TextStyle,
    val body6_2: TextStyle,
    val body7_1: TextStyle,
    val body7_2: TextStyle,
    val body7_3: TextStyle,
    val body7_4: TextStyle,
    val body8_1: TextStyle,
    val body8_2: TextStyle,
    val body9_1: TextStyle,
    val body9_2: TextStyle,
    val body9_3: TextStyle,
    val label1: TextStyle
)

internal val Typography = LogitTypography(
    headline1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    title1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    title2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    title2_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp
    ),
    title3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 19.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp
    ),
    body3_1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body3_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body3_3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body4 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 1.0.em,
        letterSpacing = 0.sp
    ),
    body5_1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body5_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body5_3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp
    ),
    body5_4 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp
    ),
    body5_5 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body6_1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 1.8.em,
        letterSpacing = 0.sp
    ),
    body6_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body7_1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body7_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body7_3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 1.6.em,
        letterSpacing = 0.sp
    ),
    body7_4 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body8_1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp
    ),
    body8_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp
    ),
    body9_1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body9_2 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    body9_3 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp
    ),
    label1 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 1.0.em,
        letterSpacing = 0.sp
    )
)

val LocalLogitTypography = staticCompositionLocalOf {
    Typography
}

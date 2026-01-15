package com.useai.core.navigation

import androidx.compose.runtime.staticCompositionLocalOf

val LocalScreenProvider = staticCompositionLocalOf<ScreenProvider> { error("ScreenProvider가 제공되지 않았습니다.") }
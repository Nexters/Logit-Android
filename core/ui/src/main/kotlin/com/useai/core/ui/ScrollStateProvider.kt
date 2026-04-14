package com.useai.core.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.staticCompositionLocalOf
import com.slack.circuit.runtime.screen.Screen

val LocalTabScrollState = staticCompositionLocalOf<Map<Screen, LazyListState>> {
    emptyMap()
}

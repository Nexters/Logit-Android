package com.useai.core.designsystem.component.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalLogitSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("LogitSnackbarHostState is not provided.")
}

package com.useai.core.ui

import androidx.compose.runtime.ProduceStateScope
import com.slack.circuit.runtime.CircuitUiState

inline fun <reified T : CircuitUiState> CircuitUiState.runOn(block: T.() -> Unit) {
    if (this is T) {
        this.block()
    }
}

inline fun<T> ProduceStateScope<T>.reduce(block: () -> T) {
    this.value = block()
}

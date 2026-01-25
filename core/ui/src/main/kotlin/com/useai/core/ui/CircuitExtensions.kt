package com.useai.core.ui

import com.slack.circuit.runtime.CircuitUiState

inline fun <reified T : CircuitUiState> CircuitUiState.runOn(block: T.() -> Unit) {
    if (this is T) {
        this.block()
    }
}

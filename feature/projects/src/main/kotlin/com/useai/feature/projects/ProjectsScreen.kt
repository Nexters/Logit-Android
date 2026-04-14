package com.useai.feature.projects

import androidx.compose.foundation.lazy.LazyListState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.model.project.ProjectListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data object ProjectsScreen : Screen {
    data class State(
        val projects: List<ProjectListItem>,
        val scrollState: LazyListState,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object NewProjectClicked : Event
        data class ProjectClicked(
            val projectId: String,
        ) : Event
    }
}

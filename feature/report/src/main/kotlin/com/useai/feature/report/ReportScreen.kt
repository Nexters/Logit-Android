package com.useai.feature.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.ReportRepository
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.ui.reduce
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data object ReportScreen : Screen {
    sealed interface State : CircuitUiState {
        data class Success(
            val summary: ExperienceSummary,
            val eventSink: (Event) -> Unit,
        ) : State

        data class LoadFailed(
            val eventSink: (Event) -> Unit,
        ) : State

        data object Loading : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Retry : Event
    }
}

class ReportPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val reportRepository: ReportRepository,
) : Presenter<ReportScreen.State> {
    @Composable
    override fun present(): ReportScreen.State {
        val scope = rememberStableCoroutineScope()
        val state by produceRetainedState<ReportScreen.State>(ReportScreen.State.Loading) {
            lateinit var fetchSummary: suspend () -> Unit

            val eventSink: (ReportScreen.Event) -> Unit = { event ->
                when (event) {
                    ReportScreen.Event.Retry -> {
                        scope.launch {
                            reduce { ReportScreen.State.Loading }
                            fetchSummary()
                        }
                    }
                }
            }

            fetchSummary = {
                reportRepository.getExperienceSummary()
                    .onSuccess { summary ->
                        reduce {
                            ReportScreen.State.Success(
                                summary = summary,
                                eventSink = eventSink
                            )
                        }
                    }
                    .onFailure {
                        reduce {
                            ReportScreen.State.LoadFailed(
                                eventSink = eventSink
                            )
                        }
                    }
            }

            fetchSummary()
        }

        return state
    }

    @AssistedFactory
    @CircuitInject(ReportScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ReportPresenter
    }
}

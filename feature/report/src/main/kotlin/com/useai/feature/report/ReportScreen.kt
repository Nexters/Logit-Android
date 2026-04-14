package com.useai.feature.report

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.AccountRepository
import com.useai.core.data.repository.ReportRepository
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.ui.LocalTabScrollState
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
            val userName: String,
            val summary: ExperienceSummary,
            val scrollState: LazyListState,
            val eventSink: (Event) -> Unit,
        ) : State

        data class LoadFailed(
            val eventSink: (Event) -> Unit,
        ) : State

        data object Loading : State
    }

    sealed interface Event : CircuitUiEvent {
        data object Retry : Event
        data object AddExperience : Event
    }
}

class ReportPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val accountRepository: AccountRepository,
    private val reportRepository: ReportRepository,
) : Presenter<ReportScreen.State> {
    @Composable
    override fun present(): ReportScreen.State {
        val scope = rememberStableCoroutineScope()
        val screenProvider = LocalScreenProvider.current
        val scrollState = LocalTabScrollState.current[ReportScreen] ?: rememberRetained { LazyListState() }
        
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

                    ReportScreen.Event.AddExperience -> {
                        navigator.goTo(screenProvider.experienceCreateScreen())
                    }
                }
            }

            fetchSummary = {
                reportRepository.getExperienceSummary()
                    .onSuccess { summary ->
                        val userName = accountRepository.getUser()
                            .getOrNull()
                            ?.fullName
                            ?.trim()
                            .orEmpty()
                        reduce {
                            ReportScreen.State.Success(
                                userName = userName,
                                summary = summary,
                                scrollState = scrollState,
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

        return if (state is ReportScreen.State.Success) {
            (state as ReportScreen.State.Success).copy(scrollState = scrollState)
        } else {
            state
        }
    }

    @AssistedFactory
    @CircuitInject(ReportScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ReportPresenter
    }
}

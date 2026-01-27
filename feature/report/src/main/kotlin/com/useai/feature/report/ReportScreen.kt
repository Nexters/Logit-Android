package com.useai.feature.report

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object ReportScreen : Screen {
    data object State : CircuitUiState
}

class ReportPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<ReportScreen.State> {
    @Composable
    override fun present(): ReportScreen.State {
        return ReportScreen.State
    }

    @AssistedFactory
    @CircuitInject(ReportScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ReportPresenter
    }
}

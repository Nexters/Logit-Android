package com.useai.feature.account

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
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
class AccountScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent
}

class AccountPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<AccountScreen.State> {
    @Composable
    override fun present(): AccountScreen.State {
        return AccountScreen.State()
    }

    @AssistedFactory
    @CircuitInject(AccountScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): AccountPresenter
    }
}

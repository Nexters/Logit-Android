package com.useai.feature.add

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
data object AddScreen : Screen {
    data object State : CircuitUiState
}

class AddPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<AddScreen.State> {
    @Composable
    override fun present(): AddScreen.State {
        return AddScreen.State
    }

    @AssistedFactory
    @CircuitInject(AddScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): AddPresenter
    }
}

package com.useai.feature.experience

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
data object ExperienceScreen : Screen {
    data object State : CircuitUiState
}

class ExperiencePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<ExperienceScreen.State> {
    @Composable
    override fun present(): ExperienceScreen.State {
        return ExperienceScreen.State
    }

    @AssistedFactory
    @CircuitInject(ExperienceScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ExperiencePresenter
    }
}

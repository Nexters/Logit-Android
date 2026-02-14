package com.useai.feature.experience

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class ExperienceCreatePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<ExperienceCreateScreen.State> {
    @Composable
    override fun present(): ExperienceCreateScreen.State {
        return ExperienceCreateScreen.State { event ->
            when (event) {
                ExperienceCreateScreen.Event.Back -> navigator.pop()
            }
        }
    }

    @AssistedFactory
    @CircuitInject(ExperienceCreateScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ExperienceCreatePresenter
    }
}

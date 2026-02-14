package com.useai.feature.experience

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.ui.reduce
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class ExperiencePresenter @AssistedInject constructor(
    private val experienceRepository: ExperienceRepository,
    @Assisted private val navigator: Navigator,
) : Presenter<ExperienceScreen.State> {
    @Composable
    override fun present(): ExperienceScreen.State {
        val scope = rememberStableCoroutineScope()
        val screenProvider = LocalScreenProvider.current

        val state by produceRetainedState<ExperienceScreen.State>(ExperienceScreen.State.Loading) {
            lateinit var fetchExperiences: suspend () -> Unit

            val eventSink: (ExperienceScreen.Event) -> Unit = { event ->
                when (event) {
                    ExperienceScreen.Event.Retry -> {
                        scope.launch {
                            reduce { ExperienceScreen.State.Loading }
                            fetchExperiences()
                        }
                    }

                    ExperienceScreen.Event.ClickAddExperience -> {
                        navigator.goTo(screenProvider.experienceCreateScreen())
                    }

                    ExperienceScreen.Event.ClickRegisterExperience -> {
                        navigator.goTo(screenProvider.experienceCreateScreen())
                    }

                    is ExperienceScreen.Event.ClickExperienceMore -> {
                        navigator.goTo(screenProvider.experienceDetailScreen(event.experienceId))
                    }
                }
            }

            fetchExperiences = {
                experienceRepository.getExperiences()
                    .onSuccess { experiences ->
                        reduce {
                            ExperienceScreen.State.Success(
                                experiences = experiences,
                                eventSink = eventSink
                            )
                        }
                    }
                    .onFailure {
                        reduce {
                            ExperienceScreen.State.LoadFailed(
                                eventSink = eventSink
                            )
                        }
                    }
            }

            fetchExperiences()
        }

        return state
    }

    @AssistedFactory
    @CircuitInject(ExperienceScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ExperiencePresenter
    }
}

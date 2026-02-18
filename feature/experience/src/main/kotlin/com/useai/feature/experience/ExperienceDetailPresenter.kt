package com.useai.feature.experience

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.component.snackbar.showLogitSnackbar
import com.useai.core.ui.reduce
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

class ExperienceDetailPresenter @AssistedInject constructor(
    private val experienceRepository: ExperienceRepository,
    @Assisted private val screen: ExperienceDetailScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<ExperienceDetailScreen.State> {
    @Composable
    override fun present(): ExperienceDetailScreen.State {
        val scope = rememberStableCoroutineScope()
        val snackbarHostState = LocalLogitSnackbarHostState.current
        val upcomingFeatureMessage = stringResource(R.string.experience_more_not_supported)

        val state by produceRetainedState<ExperienceDetailScreen.State>(ExperienceDetailScreen.State.Loading) {
            lateinit var fetchExperience: suspend () -> Unit

            val eventSink: (ExperienceDetailScreen.Event) -> Unit = { event ->
                when (event) {
                    ExperienceDetailScreen.Event.Retry -> {
                        scope.launch {
                            reduce { ExperienceDetailScreen.State.Loading }
                            fetchExperience()
                        }
                    }

                    ExperienceDetailScreen.Event.Back -> {
                        navigator.pop()
                    }

                    ExperienceDetailScreen.Event.ClickMore -> {
                        scope.launch {
                            snackbarHostState.showLogitSnackbar(
                                message = upcomingFeatureMessage,
                                iconResId = R.drawable.ic_experience_default
                            )
                        }
                    }
                }
            }

            fetchExperience = {
                experienceRepository.getExperience(screen.experienceId)
                    .onSuccess { experience ->
                        reduce {
                            ExperienceDetailScreen.State.Success(
                                experience = experience,
                                eventSink = eventSink
                            )
                        }
                    }
                    .onFailure {
                        reduce {
                            ExperienceDetailScreen.State.LoadFailed(
                                eventSink = eventSink
                            )
                        }
                    }
            }

            fetchExperience()
        }

        return state
    }

    @AssistedFactory
    @CircuitInject(ExperienceDetailScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: ExperienceDetailScreen,
            navigator: Navigator,
        ): ExperienceDetailPresenter
    }
}

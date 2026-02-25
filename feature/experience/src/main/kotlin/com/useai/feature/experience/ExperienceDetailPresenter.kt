package com.useai.feature.experience

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.model.experience.Experience
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

        var uiState by rememberRetained { mutableStateOf<ExperienceDetailScreen.State>(ExperienceDetailScreen.State.Loading) }
        var experience by rememberRetained { mutableStateOf<Experience?>(null) }
        var isMenuExpanded by rememberRetained { mutableStateOf(false) }
        var isDeleting by rememberRetained { mutableStateOf(false) }
        var isInitialized by rememberRetained { mutableStateOf(false) }
        var eventSink by rememberRetained { mutableStateOf<(ExperienceDetailScreen.Event) -> Unit>({}) }

        fun publishSuccess() {
            val current = experience ?: return
            uiState = ExperienceDetailScreen.State.Success(
                experience = current,
                isMenuExpanded = isMenuExpanded,
                isDeleting = isDeleting,
                eventSink = eventSink
            )
        }

        fun fetchExperience() {
            scope.launch {
                experienceRepository.getExperience(screen.experienceId)
                    .onSuccess { item ->
                        experience = item
                        isMenuExpanded = false
                        isDeleting = false
                        publishSuccess()
                    }
                    .onFailure {
                        uiState = ExperienceDetailScreen.State.LoadFailed(eventSink = eventSink)
                    }
            }
        }

        val editNavigator = rememberAnsweringNavigator<ExperienceCreateScreen.ExperienceSubmitResult>(
            fallbackNavigator = navigator
        ) { result ->
            if (result.action == ExperienceCreateScreen.Action.UPDATED) {
                fetchExperience()
            }
        }

        eventSink = { event ->
            when (event) {
                ExperienceDetailScreen.Event.Retry -> {
                    uiState = ExperienceDetailScreen.State.Loading
                    fetchExperience()
                }

                ExperienceDetailScreen.Event.Back -> {
                    navigator.pop()
                }

                ExperienceDetailScreen.Event.ClickMore -> {
                    isMenuExpanded = true
                    publishSuccess()
                }

                ExperienceDetailScreen.Event.DismissMenu -> {
                    isMenuExpanded = false
                    publishSuccess()
                }

                ExperienceDetailScreen.Event.ClickEdit -> {
                    val current = experience
                    if (current != null) {
                        isMenuExpanded = false
                        publishSuccess()
                        editNavigator.goTo(ExperienceCreateScreen(current.id))
                    }
                }

                ExperienceDetailScreen.Event.ClickDelete -> {
                    val current = experience
                    if (current != null) {
                        isMenuExpanded = false
                        isDeleting = true
                        publishSuccess()
                        scope.launch {
                            experienceRepository.deleteExperience(current.id)
                                .onSuccess {
                                    navigator.pop(
                                        ExperienceDetailScreen.ExperienceDeletedResult(current.id)
                                    )
                                }
                                .onFailure {
                                    isDeleting = false
                                    publishSuccess()
                                }
                        }
                    }
                }
            }
        }

        if (!isInitialized) {
            isInitialized = true
            fetchExperience()
        }

        return uiState
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

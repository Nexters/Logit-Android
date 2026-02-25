package com.useai.feature.experience

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.rememberAnsweringNavigator
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import com.useai.core.data.repository.ExperienceRepository
import com.useai.core.model.experience.Experience
import com.useai.core.navigation.LocalScreenProvider
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

        var uiState by rememberRetained { mutableStateOf<ExperienceScreen.State>(ExperienceScreen.State.Loading) }
        var experiences by rememberRetained { mutableStateOf<List<Experience>>(emptyList()) }
        var openedMenuExperienceId by rememberRetained { mutableStateOf<String?>(null) }
        var isDeleting by rememberRetained { mutableStateOf(false) }
        var isInitialized by rememberRetained { mutableStateOf(false) }
        var eventSink by rememberRetained { mutableStateOf<(ExperienceScreen.Event) -> Unit>({}) }

        fun publishSuccess() {
            uiState = ExperienceScreen.State.Success(
                experiences = experiences,
                openedMenuExperienceId = openedMenuExperienceId,
                isDeleting = isDeleting,
                eventSink = eventSink
            )
        }

        fun fetchExperiences() {
            scope.launch {
                experienceRepository.getExperiences()
                    .onSuccess { list ->
                        experiences = list
                        isDeleting = false
                        openedMenuExperienceId = null
                        publishSuccess()
                    }
                    .onFailure {
                        uiState = ExperienceScreen.State.LoadFailed(eventSink = eventSink)
                    }
            }
        }

        val detailNavigator = rememberAnsweringNavigator<ExperienceDetailScreen.ExperienceDeletedResult>(
            fallbackNavigator = navigator
        ) {
            fetchExperiences()
        }

        val createExperienceNavigator = rememberAnsweringNavigator<ExperienceCreateScreen.ExperienceSubmitResult>(
            fallbackNavigator = navigator
        ) { result ->
            when (result.action) {
                ExperienceCreateScreen.Action.CREATED -> {
                    detailNavigator.goTo(screenProvider.experienceDetailScreen(result.experienceId))
                }

                ExperienceCreateScreen.Action.UPDATED -> {
                    fetchExperiences()
                }
            }
        }

        eventSink = { event ->
            when (event) {
                ExperienceScreen.Event.Retry -> {
                    uiState = ExperienceScreen.State.Loading
                    fetchExperiences()
                }

                ExperienceScreen.Event.ClickAddExperience -> {
                    createExperienceNavigator.goTo(screenProvider.experienceCreateScreen())
                }

                ExperienceScreen.Event.ClickRegisterExperience -> {
                    createExperienceNavigator.goTo(screenProvider.experienceCreateScreen())
                }

                is ExperienceScreen.Event.ClickExperienceCard -> {
                    detailNavigator.goTo(screenProvider.experienceDetailScreen(event.experienceId))
                }

                is ExperienceScreen.Event.ClickExperienceMore -> {
                    openedMenuExperienceId = if (openedMenuExperienceId == event.experienceId) {
                        null
                    } else {
                        event.experienceId
                    }
                    publishSuccess()
                }

                ExperienceScreen.Event.DismissExperienceMenu -> {
                    openedMenuExperienceId = null
                    publishSuccess()
                }

                is ExperienceScreen.Event.ClickEditExperience -> {
                    openedMenuExperienceId = null
                    publishSuccess()
                    createExperienceNavigator.goTo(screenProvider.experienceCreateScreen(event.experienceId))
                }

                is ExperienceScreen.Event.ClickDeleteExperience -> {
                    openedMenuExperienceId = null
                    isDeleting = true
                    publishSuccess()
                    scope.launch {
                        experienceRepository.deleteExperience(event.experienceId)
                            .onSuccess {
                                fetchExperiences()
                            }
                            .onFailure {
                                isDeleting = false
                                publishSuccess()
                            }
                    }
                }
            }
        }

        if (!isInitialized) {
            isInitialized = true
            fetchExperiences()
        }

        return uiState
    }

    @AssistedFactory
    @CircuitInject(ExperienceScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): ExperiencePresenter
    }
}

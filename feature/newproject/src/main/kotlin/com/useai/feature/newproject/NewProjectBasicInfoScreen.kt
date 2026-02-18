package com.useai.feature.newproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object NewProjectBasicInfoScreen : Screen {
    data class State(
        val companyName: String,
        val jobName: String,
        val jobDesc: String,
        val talent: String,
        val showExitDialog: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class OnCompanyNameChange(val name: String) : Event
        data class OnJobNameChange(val job: String) : Event
        data class OnJobDescChange(val desc: String) : Event
        data class OnTalentChange(val talent: String) : Event
        data object Next : Event
        data object DismissExitDialog : Event
        data object ConfirmExit : Event
    }
}

class NewProjectBasicInfoPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NewProjectBasicInfoScreen.State> {
    @Composable
    override fun present(): NewProjectBasicInfoScreen.State {
        var companyName by rememberRetained { mutableStateOf("") }
        var jobName by rememberRetained { mutableStateOf("") }
        var jobDesc by rememberRetained { mutableStateOf("") }
        var talent by rememberRetained { mutableStateOf("") }
        var showExitDialog by rememberRetained { mutableStateOf(false) }
        val screenProvider = LocalScreenProvider.current

        return NewProjectBasicInfoScreen.State(
            companyName = companyName,
            jobName = jobName,
            jobDesc = jobDesc,
            talent = talent,
            showExitDialog = showExitDialog,
        ) { event ->
            when (event) {
                is NewProjectBasicInfoScreen.Event.Back -> showExitDialog = true
                is NewProjectBasicInfoScreen.Event.OnCompanyNameChange -> companyName = event.name
                is NewProjectBasicInfoScreen.Event.OnJobNameChange -> jobName = event.job
                is NewProjectBasicInfoScreen.Event.OnJobDescChange -> jobDesc = event.desc
                is NewProjectBasicInfoScreen.Event.OnTalentChange -> talent = event.talent
                is NewProjectBasicInfoScreen.Event.Next -> {
                    val newProjectQuestionScreen = screenProvider.newProjectQuestionScreen(
                        companyName = companyName,
                        jobName = jobName,
                        jobDesc = jobDesc,
                        talent = talent,
                    )
                    navigator.goTo(newProjectQuestionScreen)
                }
                is NewProjectBasicInfoScreen.Event.DismissExitDialog -> showExitDialog = false
                is NewProjectBasicInfoScreen.Event.ConfirmExit -> {
                    showExitDialog = false
                    navigator.pop()
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(NewProjectBasicInfoScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): NewProjectBasicInfoPresenter
    }
}

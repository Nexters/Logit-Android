package com.useai.feature.newproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
data object NewProjectBasicInfoScreen : Screen {
    data class State(
        val companyName: String,
        val jobName: String,
        val jobDesc: String,
        val talent: String,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class OnCompanyNameChange(val name: String) : Event
        data class OnJobNameChange(val job: String) : Event
        data class OnJobDescChange(val desc: String) : Event
        data class OnTalentChange(val talent: String) : Event
        data object Next : Event
    }
}

class NewProjectBasicInfoPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NewProjectBasicInfoScreen.State> {
    @Composable
    override fun present(): NewProjectBasicInfoScreen.State {
        var companyName by remember { mutableStateOf("") }
        var jobName by remember { mutableStateOf("") }
        var jobDesc by remember { mutableStateOf("") }
        var talent by remember { mutableStateOf("") }

        return NewProjectBasicInfoScreen.State(
            companyName = companyName,
            jobName = jobName,
            jobDesc = jobDesc,
            talent = talent,
        ) { event ->
            when (event) {
                is NewProjectBasicInfoScreen.Event.Back -> navigator.pop()
                is NewProjectBasicInfoScreen.Event.OnCompanyNameChange -> companyName = event.name
                is NewProjectBasicInfoScreen.Event.OnJobNameChange -> jobName = event.job
                is NewProjectBasicInfoScreen.Event.OnJobDescChange -> jobDesc = event.desc
                is NewProjectBasicInfoScreen.Event.OnTalentChange -> talent = event.talent
                is NewProjectBasicInfoScreen.Event.Next -> navigator.goTo(
                    NewProjectQuestionScreen(
                        companyName = companyName,
                        jobName = jobName
                    )
                )
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

package com.useai.feature.newproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

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

@HiltViewModel
class NewProjectBasicInfoViewModel @Inject constructor() : ViewModel() {
    var companyName by mutableStateOf("")
    var jobName by mutableStateOf("")
    var jobDesc by mutableStateOf("")
    var talent by mutableStateOf("")
}

class NewProjectBasicInfoPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NewProjectBasicInfoScreen.State> {
    @Composable
    override fun present(): NewProjectBasicInfoScreen.State {
        val viewModel: NewProjectBasicInfoViewModel = hiltViewModel()

        return NewProjectBasicInfoScreen.State(
            companyName = viewModel.companyName,
            jobName = viewModel.jobName,
            jobDesc = viewModel.jobDesc,
            talent = viewModel.talent,
        ) { event ->
            when (event) {
                is NewProjectBasicInfoScreen.Event.Back -> navigator.pop()
                is NewProjectBasicInfoScreen.Event.OnCompanyNameChange -> viewModel.companyName = event.name
                is NewProjectBasicInfoScreen.Event.OnJobNameChange -> viewModel.jobName = event.job
                is NewProjectBasicInfoScreen.Event.OnJobDescChange -> viewModel.jobDesc = event.desc
                is NewProjectBasicInfoScreen.Event.OnTalentChange -> viewModel.talent = event.talent
                is NewProjectBasicInfoScreen.Event.Next -> navigator.goTo(
                    NewProjectQuestionScreen(
                        companyName = viewModel.companyName,
                        jobName = viewModel.jobName,
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

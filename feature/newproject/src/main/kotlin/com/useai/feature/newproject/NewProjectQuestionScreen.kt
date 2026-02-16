package com.useai.feature.newproject

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.ProjectRepository
import com.useai.core.model.project.ProjectParam
import com.useai.core.model.project.ProjectQuestionParam
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class NewProjectQuestionScreen(
    val companyName: String,
    val jobName: String,
    val jobDesc: String,
    val talent: String,
) : Screen {
    data class State(
        val questions: List<ProjectQuestionParam> = emptyList(),
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class OnQuestionChange(val index: Int, val value: ProjectQuestionParam) : Event
        data object AddQuestion : Event
        data class DeleteQuestion(val index: Int) : Event
        data object CreateProject : Event
    }
}

class NewProjectQuestionPresenter @AssistedInject constructor(
    @Assisted private val screen: NewProjectQuestionScreen,
    @Assisted private val navigator: Navigator,
    private val projectRepository: ProjectRepository,
) : Presenter<NewProjectQuestionScreen.State> {
    val emptyQuestion = ProjectQuestionParam("", 0)

    @Composable
    override fun present(): NewProjectQuestionScreen.State {
        var questions by rememberRetained { mutableStateOf(listOf(emptyQuestion)) }
        val scope = rememberCoroutineScope()
        val screenProvider = LocalScreenProvider.current

        return NewProjectQuestionScreen.State(
            questions = questions
        ) { event ->
            when (event) {
                NewProjectQuestionScreen.Event.Back -> navigator.pop()
                is NewProjectQuestionScreen.Event.OnQuestionChange -> {
                    val newList = questions.toMutableList()
                    newList[event.index] = event.value
                    questions = newList
                }
                NewProjectQuestionScreen.Event.AddQuestion -> {
                    questions = questions + emptyQuestion
                }
                is NewProjectQuestionScreen.Event.DeleteQuestion -> {
                    val newList = questions.toMutableList()
                    newList.removeAt(event.index)
                    questions = newList
                }
                NewProjectQuestionScreen.Event.CreateProject -> {
                    scope.launch {
                        val basicInfo = screen
                        val projectQuestions = questions

                        projectRepository.createProject(
                            projectParam = ProjectParam(
                                company = basicInfo.companyName,
                                companyTalent = basicInfo.talent,
                                dueDate = LocalDate.of(2024, 12, 31), // TODO: due date 입력 폼 사양 미확정
                                jobPosition = basicInfo.jobName,
                                recruitNotice = basicInfo.jobDesc,
                                questions = projectQuestions
                            )
                        ).onSuccess {
                            val projectId = it.id
                            val chatScreen = screenProvider.chatScreen(projectId)
                            navigator.goTo(chatScreen)
                        }.onFailure {
                            Log.e(TAG, "createProject failed: $it")
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(NewProjectQuestionScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: NewProjectQuestionScreen,
            navigator: Navigator,
        ): NewProjectQuestionPresenter
    }

    companion object {
        private val TAG = NewProjectQuestionPresenter::class.java.simpleName
    }
}

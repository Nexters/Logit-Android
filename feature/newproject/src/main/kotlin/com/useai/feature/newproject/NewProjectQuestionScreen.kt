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
import com.useai.core.data.repository.ProjectsRepository
import com.useai.core.model.project.NewProject
import com.useai.core.model.project.NewQuestionData
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewProjectQuestionScreen(
    val companyName: String,
    val jobName: String,
    val jobDesc: String,
    val talent: String,
) : Screen {
    data class State(
        val questions: List<NewQuestionData>,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class OnQuestionChange(val index: Int, val value: NewQuestionData) : Event
        data object AddQuestion : Event
        data class DeleteQuestion(val index: Int) : Event
        data object CreateProject : Event
    }
}

class NewProjectQuestionPresenter @AssistedInject constructor(
    @Assisted private val screen: NewProjectQuestionScreen,
    @Assisted private val navigator: Navigator,
    private val projectsRepository: ProjectsRepository,
) : Presenter<NewProjectQuestionScreen.State> {
    @Composable
    override fun present(): NewProjectQuestionScreen.State {
        var questions by rememberRetained { mutableStateOf(listOf(NewQuestionData())) }
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
                    questions = questions + NewQuestionData()
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

                        projectsRepository.createNewProject(
                            newProject = NewProject(
                                companyName = basicInfo.companyName,
                                jobName = basicInfo.jobName,
                                jobDesc = basicInfo.jobDesc,
                                talent = basicInfo.talent,
                                questions = projectQuestions,
                                dueDate = "2024-12-31" // TODO: due date 입력 폼 추가
                            )
                        ).onSuccess {
                            val projectId = it.projectId
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

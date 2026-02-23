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
        val questions: List<ProjectQuestionParam>,
        val isButtonEnabled: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data object LoadExample : Event
        data class QuestionChanged(val index: Int, val value: String) : Event
        data class MaxLengthChanged(val index: Int, val value: Int) : Event
        data object AddQuestionClicked : Event
        data class DeleteQuestionClicked(val index: Int) : Event
        data object FinishClicked : Event
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
            questions = questions,
            isButtonEnabled = questions.any { it.question.isNotBlank() },
        ) { event ->
            when (event) {
                NewProjectQuestionScreen.Event.Back -> navigator.pop()
                NewProjectQuestionScreen.Event.LoadExample -> {
                    questions = SAMPLE_QUESTIONS
                }

                is NewProjectQuestionScreen.Event.QuestionChanged -> {
                    val oldValue = questions[event.index]
                    questions = questions.update(event.index, oldValue.copy(question = event.value))
                }

                is NewProjectQuestionScreen.Event.MaxLengthChanged -> {
                    val oldValue = questions[event.index]
                    questions = questions.update(event.index, oldValue.copy(maxLength = event.value))
                    Log.d(TAG, questions.toString())
                }

                NewProjectQuestionScreen.Event.AddQuestionClicked -> {
                    questions = questions + emptyQuestion
                }

                is NewProjectQuestionScreen.Event.DeleteQuestionClicked -> {
                    val newList = questions.toMutableList()
                    newList.removeAt(event.index)
                    questions = newList
                }

                NewProjectQuestionScreen.Event.FinishClicked -> {
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
                            navigator.resetRoot(screenProvider.homeScreen())
                            navigator.goTo(chatScreen)
                        }.onFailure {
                            Log.e(TAG, "createProject failed: $it")
                        }
                    }
                }
            }
        }
    }

    private fun <T> List<T>.update(index: Int, item: T): List<T> {
        return toMutableList().apply {
            set(index, item)
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
        private val SAMPLE_QUESTIONS = listOf(
            ProjectQuestionParam(
                question = "로짓(Logit)에 지원하게 된 동기와, 본인이 서비스 기획자로서 가진 차별화된 역량을 실제 경험을 바탕으로 기술해 주세요.",
                maxLength = 800
            ),
            ProjectQuestionParam(
                question = "기존의 방식이나 서비스에서 불편함을 포착하여, 논리적인 분석을 통해 실질적인 해결책을 제시하고 성과를 낸 경험을 구체적으로 기술해 주세요.",
                maxLength = 700
            ),
            ProjectQuestionParam(
                question = "다양한 이해관계자(개발자, 디자이너 등)와 협업하는 과정에서 의견 차이를 극복하고, 사용자의 관점에서 최선의 결과를 도출했던 경험을 기술해 주세요.",
                maxLength = 600
            )
        )
    }
}

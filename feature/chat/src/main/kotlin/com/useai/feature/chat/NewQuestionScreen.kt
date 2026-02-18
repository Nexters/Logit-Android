package com.useai.feature.chat

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.useai.core.data.repository.QuestionRepository
import com.useai.core.model.project.ProjectQuestionParam
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewQuestionScreen(val projectId: String) : Screen {
    data class State(
        val question: String,
        val maxLength: Int,
        val isButtonEnabled: Boolean,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data class QuestionChanged(val question: String) : Event
        data class MaxLengthChanged(val maxLength: Int) : Event
        data object ConfirmClicked : Event
    }
}

class NewQuestionPresenter @AssistedInject constructor(
    @Assisted private val screen: NewQuestionScreen,
    @Assisted private val navigator: Navigator,
    private val questionRepository: QuestionRepository,
) : Presenter<NewQuestionScreen.State> {
    @Composable
    override fun present(): NewQuestionScreen.State {
        var question by rememberRetained { mutableStateOf("") }
        var maxLength by rememberRetained { mutableIntStateOf(0) }
        val scope = rememberCoroutineScope()

        return NewQuestionScreen.State(
            question = question,
            maxLength = maxLength,
            isButtonEnabled = question.isNotBlank() && maxLength > 0,
        ) { event ->
            when (event) {
                NewQuestionScreen.Event.Back -> {
                    navigator.pop()
                }

                is NewQuestionScreen.Event.QuestionChanged -> {
                    question = event.question
                }

                is NewQuestionScreen.Event.MaxLengthChanged -> {
                    maxLength = event.maxLength
                }

                NewQuestionScreen.Event.ConfirmClicked -> {
                    scope.launch {
                        questionRepository.createQuestion(
                            projectId = screen.projectId,
                            question = ProjectQuestionParam(
                                question = question,
                                maxLength = maxLength,
                            )
                        ).onSuccess {
                            navigator.pop()
                        }.onFailure {
                            Log.e(TAG, "createQuestion failed: $it")
                        }
                    }
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(NewQuestionScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            screen: NewQuestionScreen,
            navigator: Navigator,
        ): NewQuestionPresenter
    }

    companion object {
        private val TAG = NewQuestionPresenter::class.simpleName
    }
}

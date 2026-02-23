package com.useai.feature.chat

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.screen.PopResult
import com.useai.core.designsystem.R
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.Question
import com.useai.core.model.experience.MatchingExperience
import com.useai.core.model.project.Project
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatScreen(val projectId: String): Screen {
    @Parcelize
    data class ProjectDeletedResult(
        val projectId: String,
    ) : PopResult

    sealed interface State : CircuitUiState {

        data class Success(
            val project: Project,
            val questions: List<Question>,
            val currentQuestion: Question,
            val currentCategory: ChatScreenCategory,
            val chattingHistory: ChattingHistory,
            val matchingExperiences: List<MatchingExperience>,
            val streamingStatus: ChattingStreamingStatus,
            val userInput: String,
            val isHeaderUIExpanded: Boolean,
            val showExperienceModal: Boolean = false,
            val showDeleteDialog: Boolean = false,
            val eventSink: (Event) -> Unit
        ) : State
        data class Loading(
            val eventSink: (Event) -> Unit,
        ) : State

        data class LoadFailed(
            val eventSink: (Event) -> Unit,
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data class ChangeQuestion(val question: Question) : Event
        data class ChangeCategory(val category: ChatScreenCategory) : Event
        data class RefreshData(val preferredQuestionId: String? = null) : Event
        data class AddCreatedQuestion(val question: Question) : Event
        data class ApplyEditedQuestions(val questions: List<EditQuestionsScreen.EditedQuestionResult>) : Event
        data object EditQuestions : Event
        data object AddQuestion : Event
        data object TryUploadExperience : Event
        data class CompleteSelectExperience(val experienceIds: List<String>) : Event
        data class GenerateDraft(val experienceIds: List<String>) : Event
        data object DismissExperienceModal : Event
        data class InputMessage(val message: String) : Event
        data class SendMessage(val message: String) : Event
        data class CopyMessage(val message: String) : Event
        data class UpdateLetter(val letter: String) : Event
        data object TryDeleteProject : Event
        data object DismissDeleteDialog : Event
        data object ConfirmDeleteProject : Event
        data object ExpandOrShrinkHeader : Event
        data object NavigateBack : Event
    }

}

sealed interface ChattingStreamingStatus {
    data object Idle: ChattingStreamingStatus
    data object Loading: ChattingStreamingStatus
    data class Streaming(val data: String): ChattingStreamingStatus
    data object Error: ChattingStreamingStatus
}

enum class ChatScreenCategory(
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int
) {
    CHATTING(R.string.chatting, R.drawable.ic_chatting),
    LETTER(R.string.letter, R.drawable.ic_intro)
}

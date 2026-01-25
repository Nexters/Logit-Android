package com.useai.feature.chat

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.designsystem.R
import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.Question
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatScreen(val projectId: String): Screen {
    sealed interface State : CircuitUiState {

        data class Chatting(
            val questions: List<Question>,
            val currentQuestion: Question,
            val chattingHistory: ChattingHistory,
            val streamingStatus: ChattingStreamingStatus,
            val userInput: String,
            val eventSink: (Event) -> Unit
        ) : State
        data class Letter(
            val questions: List<Question>,
            val currentQuestion: Question,
            val content: String,
            val userInput: String,
            val eventSink: (Event) -> Unit
        ) : State
        data object Loading : State
        data object LoadFailed : State
    }

    sealed interface Event : CircuitUiEvent {
        data class ChangeQuestion(val question: Question) : Event
        data class ChangeCategory(val category: ChatScreenCategory) : Event
        data object AddQuestion : Event
        data object UploadExperience : Event
        data class InputMessage(val message: String) : Event

        sealed interface Chatting : Event {
            data class SendMessage(val message: String) : Chatting
            data class CopyMessage(val message: String) : Chatting
            data class UpdateLetter(val letter: String) : Chatting
        }
        sealed interface Letter : Event {
            data class SendMessage(val message: String) : Letter
        }
    }

}

sealed interface ChattingStreamingStatus {
    data object Idle: ChattingStreamingStatus
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

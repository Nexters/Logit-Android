package com.useai.core.network.response

import com.useai.core.model.chat.ChattingStreaming
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChattingStreamingResponse(
    @SerialName("type") val type: String,
    @SerialName("content") val data: String? = null,
    @SerialName("chat_id") val chatId: String? = null,
    @SerialName("is_draft") val isDraft: Boolean? = null,
    @SerialName("remaining_chats") val remainingChats: Int? = null
)

fun ChattingStreamingResponse.toChattingStreaming() : ChattingStreaming {
    return when (type) {
        "content" -> {
            ChattingStreaming.Streaming(data.orEmpty())
        }
        "done" -> {
            ChattingStreaming.Done(chatId.orEmpty(), isDraft ?: false)
        }
        else -> {
            throw IllegalArgumentException("Unknown type: $type")
        }
    }
}

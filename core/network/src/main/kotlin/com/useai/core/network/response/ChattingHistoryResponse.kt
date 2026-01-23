package com.useai.core.network.response

import com.useai.core.model.chat.ChattingContent
import com.useai.core.model.chat.ChattingHistory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChattingHistoryResponse(
    @SerialName("chats") val chats: List<ChattingContentResponse>,
    @SerialName("created_at") val createdAt: String,
    @SerialName("experience_ids") val experienceIds: List<Int>,
    @SerialName("project_name") val projectName: String,
    @SerialName("question") val questionTitle: String,
    @SerialName("question_id") val questionId: Int,
)

@Serializable
data class ChattingContentResponse(
    @SerialName("id") val id: String,
    @SerialName("content") val content: String,
    @SerialName("is_draft") val isDraft: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("role") val role: String,
)

fun ChattingHistoryResponse.toChattingHistory() = ChattingHistory(
    chattings = chats.map { it.toChattingContent() }
)

fun ChattingContentResponse.toChattingContent() : ChattingContent {
    return when (role) {
        "user" -> {
            ChattingContent.User(
                id = id,
                message = content,
                createdAt = createdAt
            )
        }
        "ai" -> {
            ChattingContent.AI(
                id = id,
                message = content,
                createdAt = createdAt,
                isLetter = isDraft
            )
        }
        else -> {
            throw IllegalArgumentException("Unknown role: $role")
        }
    }
}

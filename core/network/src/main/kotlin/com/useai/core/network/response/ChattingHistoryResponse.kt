package com.useai.core.network.response

import com.useai.core.common.extensions.toLocalDate
import com.useai.core.common.extensions.toLocalDateTime
import com.useai.core.model.chat.ChattingContent
import com.useai.core.model.chat.ChattingHistory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChattingHistoryResponse(
    @SerialName("chats") val chats: List<ChattingContentResponse>,
    @SerialName("project_created_at") val projectCreatedAt: String,
    @SerialName("experience_ids") val experienceIds: List<String>,
    @SerialName("project_name") val projectName: String,
    @SerialName("question") val questionTitle: String,
    @SerialName("question_id") val questionId: String,
    @SerialName("next_cursor") val nextCursor: String?,
    @SerialName("has_more") val hasMore: Boolean,
    @SerialName("remaining_chats") val remainingChats: Int
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
    chattings = chats.map { it.toChattingContent() },
    projectCreatedAt = projectCreatedAt.toLocalDateTime() ?: LocalDateTime.MIN,
    experienceIds = experienceIds,
    questionId = questionId,
    projectName = projectName,
    questionTitle = questionTitle,
    nextCursor = nextCursor,
    hasMore = hasMore,
    remainingChats = remainingChats
)

fun ChattingContentResponse.toChattingContent() : ChattingContent {
    return when (role) {
        "user" -> {
            ChattingContent.User(
                id = id,
                message = content,
                createdAt = createdAt.toLocalDateTime() ?: LocalDateTime.MIN
            )
        }
        "assistant" -> {
            ChattingContent.AI(
                id = id,
                message = content,
                createdAt = createdAt.toLocalDateTime() ?: LocalDateTime.MIN,
                isLetter = isDraft
            )
        }
        else -> {
            throw IllegalArgumentException("Unknown role: $role")
        }
    }
}

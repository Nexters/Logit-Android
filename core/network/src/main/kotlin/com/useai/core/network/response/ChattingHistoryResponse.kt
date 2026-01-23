package com.useai.core.network.response

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

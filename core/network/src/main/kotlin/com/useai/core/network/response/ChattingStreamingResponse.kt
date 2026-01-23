package com.useai.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChattingStreamingResponse(
    @SerialName("type") val type: String,
    @SerialName("content") val data: String? = null,
    @SerialName("chat_id") val chatId: String? = null,
    @SerialName("is_draft") val isDraft: String? = null,
)

package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartChattingStreamRequest(
    @SerialName("content") val sendingMessage: String,
    @SerialName("experience_ids") val experienceIds: List<String>, // Changed from List<Int> to List<String>
    @SerialName("question_id") val questionId: String
)

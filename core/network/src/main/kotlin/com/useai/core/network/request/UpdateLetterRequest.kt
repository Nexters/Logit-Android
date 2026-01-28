package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateLetterRequest(
    @SerialName("question_id") val questionId: String,
    @SerialName("answer") val content: String
)

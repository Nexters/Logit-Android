package com.useai.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    @SerialName("id") val id: String,
    @SerialName("answer") val letter: String,
    @SerialName("question") val title: String,
    @SerialName("max_length") val maxLength: Int
)

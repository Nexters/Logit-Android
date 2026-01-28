package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateQuestionRequest(
    @SerialName("question") val title: String,
    @SerialName("max_length") val maxLength: Int,
    @SerialName("answer") val letter: String
)

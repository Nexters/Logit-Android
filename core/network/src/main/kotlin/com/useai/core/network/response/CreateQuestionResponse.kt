package com.useai.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateQuestionResponse(
    @SerialName("id") val questionId: String
)

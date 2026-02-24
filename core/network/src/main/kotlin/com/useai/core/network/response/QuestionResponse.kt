package com.useai.core.network.response

import com.useai.core.model.chat.Question
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    @SerialName("id") val id: String,
    @SerialName("answer") val letter: String?,
    @SerialName("question") val title: String,
    @SerialName("max_length") val maxLength: Int?,
    @SerialName("is_completed") val isCompleted: Boolean,
)

fun QuestionResponse.toQuestion() = Question(
    id = id,
    title = title,
    maxLength = maxLength ?: 0,
    letter = letter.orEmpty(),
    isCompleted = isCompleted,
)

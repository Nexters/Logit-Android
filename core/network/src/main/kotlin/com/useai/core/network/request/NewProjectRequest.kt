package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewProjectRequest(
    val company: String,
    @SerialName("company_talent") val talent: String,
    @SerialName("due_date") val dueDate: String,
    @SerialName("job_position") val jobName: String,
    val questions: List<Question>,
    @SerialName("recruit_notice") val jobDesc: String,
)

@Serializable
data class Question(
    @SerialName("max_length") val maxLength: Int,
    val question: String,
)

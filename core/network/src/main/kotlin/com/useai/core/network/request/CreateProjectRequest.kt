package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectRequest(
    @SerialName("company") val company: String,
    @SerialName("company_talent") val companyTalent: String,
    @SerialName("due_date") val dueDate: String,
    @SerialName("job_position") val jobPosition: String,
    @SerialName("questions") val questions: List<ProjectQuestionRequest>,
    @SerialName("recruit_notice") val recruitNotice: String
)

@Serializable
data class ProjectQuestionRequest(
    @SerialName("max_length") val maxLength: Int,
    @SerialName("question") val question: String
)

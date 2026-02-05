package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    @SerialName("company") val company: String? = null,
    @SerialName("company_talent") val companyTalent: String? = null,
    @SerialName("due_date") val dueDate: String? = null,
    @SerialName("job_position") val jobPosition: String? = null
)

package com.useai.core.network.response

import com.useai.core.model.project.Project
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewProjectResponse(
    val company: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("due_date") val dueDate: String,
    val id: String,
    @SerialName("job_position") val jobPosition: String,
    @SerialName("recruit_notice") val recruitNotice: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("user_id") val userId: String,
)

fun NewProjectResponse.toProject(): Project {
    return Project(
        userId = userId,
        projectId = id,
        companyName = company,
        jobName = jobPosition,
        jobDesc = recruitNotice,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

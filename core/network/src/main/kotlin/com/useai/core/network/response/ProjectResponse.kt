package com.useai.core.network.response

import com.useai.core.common.extensions.toLocalDate
import com.useai.core.common.extensions.toLocalDateTime
import com.useai.core.model.project.Project
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class ProjectResponse(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("company") val company: String,
    @SerialName("due_date") val dueDate: String,
    @SerialName("job_position") val jobPosition: String,
    @SerialName("recruit_notice") val recruitNotice: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)

fun ProjectResponse.toProject() = Project(
    id = id,
    company = company,
    dueDate = dueDate.toLocalDate() ?: LocalDate.MIN,
    jobPosition = jobPosition,
    recruitNotice = recruitNotice,
    createdAt = createdAt.toLocalDateTime() ?: LocalDateTime.MIN,
    updatedAt = updatedAt.toLocalDateTime() ?: LocalDateTime.MIN
)

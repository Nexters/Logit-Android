package com.useai.core.network.response

import com.useai.core.common.extensions.toLocalDate
import com.useai.core.common.extensions.toLocalDateTime
import com.useai.core.model.project.Project
import com.useai.core.model.project.ProjectListItem
import com.useai.core.model.project.QuestionItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class ProjectWithQuestionResponse(
    val project: ProjectResponse,
    val questions: List<QuestionItemResponse>,
)

@Serializable
data class ProjectResponse(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("company") val company: String,
    @SerialName("due_date") val dueDate: String?,
    @SerialName("job_position") val jobPosition: String,
    @SerialName("recruit_notice") val recruitNotice: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)

@Serializable
data class QuestionItemResponse(
    @SerialName("id") val id: String,
    val question: String,
    @SerialName("max_length") val maxLength: Int,
)

fun ProjectWithQuestionResponse.toProject() = Project(
    id = project.id,
    company = project.company,
    dueDate = project.dueDate.toLocalDate() ?: LocalDate.MIN,
    jobPosition = project.jobPosition,
    recruitNotice = project.recruitNotice,
    createdAt = project.createdAt.toLocalDateTime() ?: LocalDateTime.MIN,
    updatedAt = project.updatedAt.toLocalDateTime() ?: LocalDateTime.MIN,
    questions = questions.map {
        QuestionItem(
            id = it.id,
            question = it.question,
            maxLength = it.maxLength,
        )
    }
)

@Serializable
data class ProjectListItemResponse(
    @SerialName("id") val id: String,
    @SerialName("company") val company: String,
    @SerialName("job_position") val jobPosition: String,
    @SerialName("due_date") val dueDate: String?,
    @SerialName("question_id") val questionId: String?,
    @SerialName("total_questions") val totalQuestions: Int,
    @SerialName("completed_questions") val completedQuestions: Int,
    @SerialName("updated_at") val updatedAt: String,
)

fun ProjectListItemResponse.toProjectListItem() = ProjectListItem(
    id = id,
    company = company,
    jobPosition = jobPosition,
    dueDate = dueDate.toLocalDate() ?: LocalDate.MAX,
    questionId = questionId ?: "",
    totalQuestions = totalQuestions,
    completedQuestions = completedQuestions,
    updatedAt = updatedAt.toLocalDateTime() ?: LocalDateTime.MIN
)

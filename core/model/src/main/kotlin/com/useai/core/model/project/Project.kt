package com.useai.core.model.project

import java.time.LocalDate
import java.time.LocalDateTime

data class Project(
    val id: String,
    val company: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val dueDate: LocalDate,
    val jobPosition: String,
    val recruitNotice: String,
)

data class ProjectParam(
    val company: String,
    val companyTalent: String,
    val dueDate: LocalDate,
    val jobPosition: String,
    val recruitNotice: String,
    val questions: List<ProjectQuestionParam>
)

data class ProjectQuestionParam(
    val question: String,
    val maxLength: Int
)

data class ProjectListItem(
    val id: String,
    val company: String,
    val jobPosition: String,
    val dueDate: LocalDate,
    val questionId: String,
    val totalQuestions: Int,
    val completedQuestions: Int,
    val updatedAt: LocalDateTime
)

data class UpdateProjectParam(
    val company: String? = null,
    val companyTalent: String? = null,
    val dueDate: LocalDate? = null,
    val jobPosition: String? = null,
)

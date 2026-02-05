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

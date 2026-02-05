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

package com.useai.core.model.project

import java.time.LocalDateTime

data class Project(
    val userId: String,
    val projectId: String,
    val companyName: String,
    val jobName: String,
    val jobDesc: String,
    val dueDate: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

package com.useai.core.model.project

data class Project(
    val userId: String,
    val projectId: String,
    val companyName: String,
    val jobName: String,
    val jobDesc: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
)

package com.useai.core.model.project

data class NewProject(
    val companyName: String,
    val jobName: String,
    val jobDesc: String,
    val talent: String,
    val questions: List<NewQuestionData>,
    val dueDate: String,
)

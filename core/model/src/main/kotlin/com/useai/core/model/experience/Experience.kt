package com.useai.core.model.experience

data class Experience(
    val id: String,
    val tags: List<String>,
    val situation: String,
    val task: String,
    val action: String,
    val result: String,
    val category: ExperienceCategory,
    val date: String,
    val experienceType: String,
    val title: String
)

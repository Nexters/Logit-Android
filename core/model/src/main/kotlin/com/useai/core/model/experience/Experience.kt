package com.useai.core.model.experience

import java.time.LocalDate

data class Experience(
    val id: String,
    val tags: List<String>,
    val situation: String,
    val task: String,
    val action: String,
    val result: String,
    val category: ExperienceCategory,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val experienceType: String,
    val formatType: String?,
    val title: String
)

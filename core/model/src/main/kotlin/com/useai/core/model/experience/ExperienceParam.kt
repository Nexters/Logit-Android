package com.useai.core.model.experience

import java.time.LocalDate

data class ExperienceParam(
    val situation: String,
    val task: String,
    val action: String,
    val result: String,
    val category: String,
    val date: LocalDate,
    val experienceType: String,
    val title: String
)

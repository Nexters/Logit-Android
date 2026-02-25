package com.useai.core.model.experience

import java.time.LocalDate

data class ExperienceParam(
    val title: String,
    val experienceType: String,
    val formatType: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val tags: String,
    val situation: String,
    val task: String,
    val action: String,
    val result: String,
)

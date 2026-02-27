package com.useai.core.model.experience

import java.time.LocalDate

data class ExperienceParam(
    val title: String,
    val experienceType: String,
    val formatType: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val tags: String,
    val situation: String? = null,
    val task: String? = null,
    val action: String? = null,
    val result: String? = null,
    val problem: String? = null,
    val solution: String? = null,
    val insight: String? = null,
    val content: String? = null,
)

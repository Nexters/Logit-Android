package com.useai.core.model.report

import com.useai.core.model.experience.ExperienceCategory

data class ExperienceTypeCount(
    val type: ExperienceReportType,
    val count: Int,
)

data class ExperienceCategoryCount(
    val category: ExperienceCategory,
    val count: Int,
)

data class ExperienceTagCount(
    val tag: String,
    val count: Int,
)

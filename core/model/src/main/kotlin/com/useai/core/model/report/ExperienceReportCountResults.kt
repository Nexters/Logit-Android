package com.useai.core.model.report

data class ExperienceTypeCountResult(
    val data: List<ExperienceTypeCount>,
    val total: Int,
)

data class ExperienceCategoryCountResult(
    val data: List<ExperienceCategoryCount>,
    val total: Int,
)

data class ExperienceTagCountResult(
    val data: List<ExperienceTagCount>,
    val total: Int,
)

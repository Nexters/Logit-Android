package com.useai.core.model.report

data class ExperienceSummary(
    val typeCounts: List<ExperienceTypeCount>,
    val categoryCounts: List<ExperienceCategoryCount>,
    val tagCounts: List<ExperienceTagCount>,
    val total: Int,
)

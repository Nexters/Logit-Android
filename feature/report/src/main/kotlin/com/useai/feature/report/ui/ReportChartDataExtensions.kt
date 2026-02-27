package com.useai.feature.report.ui

import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.report.ExperienceCategoryCount
import com.useai.core.model.report.ExperienceReportType
import com.useai.core.model.report.ExperienceTagCount
import com.useai.core.model.report.ExperienceTypeCount

private val verticalChartPaddingPriority = listOf(
    ExperienceCategory.CUSTOMER_VALUE_ORIENTATION,
    ExperienceCategory.TECHNICAL_EXPERTISE,
    ExperienceCategory.COLLABORATIVE_COMMUNICATION,
    ExperienceCategory.PROACTIVE_EXECUTION,
    ExperienceCategory.LOGICAL_ANALYSIS,
    ExperienceCategory.CREATIVE_PROBLEM_SOLVING,
)

internal fun List<ExperienceCategoryCount>.toVerticalChartData(maxSize: Int = 6): List<ExperienceCategoryCount> {
    val base = sortedByDescending { it.count }.take(maxSize)
    if (base.size >= maxSize) return base

    val includedCategories = base.map { it.category }.toSet()
    val needed = maxSize - base.size
    val padding = verticalChartPaddingPriority
        .asSequence()
        .filter { it !in includedCategories }
        .map { category -> ExperienceCategoryCount(category = category, count = 0) }
        .take(needed)
        .toList()

    return base + padding
}

private val horizontalChartPaddingPriority = listOf(
    ExperienceReportType.PART_TIME,
    ExperienceReportType.FULL_TIME,
    ExperienceReportType.INTERN,
    ExperienceReportType.CONTRACT,
    ExperienceReportType.VOLUNTEER,
    ExperienceReportType.CLUB,
)

internal fun List<ExperienceTypeCount>.toHorizontalChartData(maxSize: Int = 6): List<ExperienceTypeCount> {
    val base = sortedByDescending { it.count }.take(maxSize)
    if (base.size >= maxSize) return base

    val includedTypes = base.map { it.type }.toSet()
    val needed = maxSize - base.size
    val padding = horizontalChartPaddingPriority
        .asSequence()
        .filter { it !in includedTypes }
        .map { type -> ExperienceTypeCount(type = type, count = 0) }
        .take(needed)
        .toList()

    return base + padding
}

private val donutChartPaddingPriority = listOf(
    "백엔드",
    "DB 설계",
    "AI/LLM",
    "코드리뷰",
    "리서치",
    "문제해결",
)

internal fun List<ExperienceTagCount>.toDonutChartData(maxSize: Int = 6): List<ExperienceTagCount> {
    if (isEmpty()) {
        return donutChartPaddingPriority
            .take(maxSize)
            .map { tag -> ExperienceTagCount(tag = tag, count = 0) }
    }

    return sortedByDescending { it.count }
        .filter { it.count > 0 }
        .take(maxSize)
}

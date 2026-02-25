package com.useai.feature.report.ui

import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.report.ExperienceCategoryCount
import com.useai.core.model.report.ExperienceReportType
import com.useai.core.model.report.ExperienceTagCount
import com.useai.core.model.report.ExperienceTypeCount

private val verticalChartPaddingPriority = listOf(
    ExperienceCategory.CUSTOMER_VALUE_ORIENTATION,   // 고객 중심
    ExperienceCategory.TECHNICAL_EXPERTISE,          // 전문성
    ExperienceCategory.COLLABORATIVE_COMMUNICATION,  // 소통력
    ExperienceCategory.PROACTIVE_EXECUTION,          // 실행력
    ExperienceCategory.LOGICAL_ANALYSIS,             // 분석력
    ExperienceCategory.CREATIVE_PROBLEM_SOLVING,     // 문제해결력
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
    ExperienceReportType.PART_TIME, // 아르바이트
    ExperienceReportType.FULL_TIME, // 정규직
    ExperienceReportType.INTERN,    // 인턴
    ExperienceReportType.CONTRACT,  // 계약직
    ExperienceReportType.VOLUNTEER, // 봉사 활동
    ExperienceReportType.CLUB,      // 동아리 활동
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
    "앱개발",
    "DB 설계",
    "AI/LLM",
    "코드리뷰",
    "리서치",
    "문제해결",
)

private fun String.normalizeTagKey(): String = lowercase().replace(" ", "")

internal fun List<ExperienceTagCount>.toDonutChartData(maxSize: Int = 6): List<ExperienceTagCount> {
    val base = sortedByDescending { it.count }.take(maxSize)
    if (base.size >= maxSize) return base

    val includedTagKeys = base.map { it.tag.normalizeTagKey() }.toSet()
    val needed = maxSize - base.size
    val padding = donutChartPaddingPriority
        .asSequence()
        .filter { it.normalizeTagKey() !in includedTagKeys }
        .map { tag -> ExperienceTagCount(tag = tag, count = 0) }
        .take(needed)
        .toList()

    return base + padding
}

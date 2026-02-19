package com.useai.core.network.response

import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.report.ExperienceCategoryCount
import com.useai.core.model.report.ExperienceCategoryCountResult
import com.useai.core.model.report.ExperienceReportType
import com.useai.core.model.report.ExperienceSummary
import com.useai.core.model.report.ExperienceTagCount
import com.useai.core.model.report.ExperienceTagCountResult
import com.useai.core.model.report.ExperienceTypeCount
import com.useai.core.model.report.ExperienceTypeCountResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperienceSummaryResponse(
    @SerialName("type_counts") val typeCounts: List<ExperienceTypeCountItemResponse>,
    @SerialName("category_counts") val categoryCounts: List<ExperienceCategoryCountItemResponse>,
    @SerialName("tag_counts") val tagCounts: List<ExperienceTagCountItemResponse>,
    @SerialName("total") val total: Int,
)

@Serializable
data class ExperienceTypeCountResponse(
    @SerialName("data") val data: List<ExperienceTypeCountItemResponse>,
    @SerialName("total") val total: Int,
)

@Serializable
data class ExperienceCategoryCountResponse(
    @SerialName("data") val data: List<ExperienceCategoryCountItemResponse>,
    @SerialName("total") val total: Int,
)

@Serializable
data class ExperienceTagCountResponse(
    @SerialName("data") val data: List<ExperienceTagCountItemResponse>,
    @SerialName("total") val total: Int,
)

@Serializable
data class ExperienceTypeCountItemResponse(
    @SerialName("type") val type: String,
    @SerialName("count") val count: Int,
)

@Serializable
data class ExperienceCategoryCountItemResponse(
    @SerialName("category") val category: String,
    @SerialName("count") val count: Int,
)

@Serializable
data class ExperienceTagCountItemResponse(
    @SerialName("tag") val tag: String,
    @SerialName("count") val count: Int,
)

fun ExperienceSummaryResponse.toExperienceSummary(): ExperienceSummary {
    return ExperienceSummary(
        typeCounts = typeCounts.map { it.toExperienceTypeCount() },
        categoryCounts = categoryCounts.map { it.toExperienceCategoryCount() },
        tagCounts = tagCounts.map { it.toExperienceTagCount() },
        total = total
    )
}

fun ExperienceTypeCountResponse.toExperienceTypeCountResult(): ExperienceTypeCountResult {
    return ExperienceTypeCountResult(
        data = data.map { it.toExperienceTypeCount() },
        total = total
    )
}

fun ExperienceCategoryCountResponse.toExperienceCategoryCountResult(): ExperienceCategoryCountResult {
    return ExperienceCategoryCountResult(
        data = data.map { it.toExperienceCategoryCount() },
        total = total
    )
}

fun ExperienceTagCountResponse.toExperienceTagCountResult(): ExperienceTagCountResult {
    return ExperienceTagCountResult(
        data = data.map { it.toExperienceTagCount() },
        total = total
    )
}

private fun ExperienceTypeCountItemResponse.toExperienceTypeCount(): ExperienceTypeCount {
    return ExperienceTypeCount(
        type = type.toExperienceReportType(),
        count = count
    )
}

private fun ExperienceCategoryCountItemResponse.toExperienceCategoryCount(): ExperienceCategoryCount {
    return ExperienceCategoryCount(
        category = category.toExperienceCategory(),
        count = count
    )
}

private fun ExperienceTagCountItemResponse.toExperienceTagCount(): ExperienceTagCount {
    return ExperienceTagCount(
        tag = tag,
        count = count
    )
}

private fun String.toExperienceReportType(): ExperienceReportType {
    return when (trim()) {
        "아르바이트" -> ExperienceReportType.PART_TIME
        "인턴" -> ExperienceReportType.INTERN
        "정규직" -> ExperienceReportType.FULL_TIME
        "계약직" -> ExperienceReportType.CONTRACT
        "봉사 활동" -> ExperienceReportType.VOLUNTEER
        "수상경력" -> ExperienceReportType.AWARD
        "동아리 활동" -> ExperienceReportType.CLUB
        "연구 활동" -> ExperienceReportType.RESEARCH
        "군복무" -> ExperienceReportType.MILITARY
        "개인 활동" -> ExperienceReportType.PERSONAL
        else -> ExperienceReportType.UNKNOWN
    }
}

private fun String.toExperienceCategory(): ExperienceCategory {
    return when (trim()) {
        "주도적 실행력" -> ExperienceCategory.PROACTIVE_EXECUTION
        "기술적 전문성" -> ExperienceCategory.TECHNICAL_EXPERTISE
        "논리적 분석력" -> ExperienceCategory.LOGICAL_ANALYSIS
        "창의적 문제해결" -> ExperienceCategory.CREATIVE_PROBLEM_SOLVING
        "협력적 소통" -> ExperienceCategory.COLLABORATIVE_COMMUNICATION
        "끈기있는 책임감" -> ExperienceCategory.TENACIOUS_RESPONSIBILITY
        "유연한 적응력" -> ExperienceCategory.FLEXIBLE_ADAPTABILITY
        "고객 가치 지향" -> ExperienceCategory.CUSTOMER_VALUE_ORIENTATION
        else -> error("Unknown experience category: $this")
    }
}

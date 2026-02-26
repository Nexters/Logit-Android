package com.useai.core.network.response

import com.useai.core.common.extensions.toLocalDate
import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.model.experience.ExperienceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ExperienceResponse(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String?, // Changed to nullable String
    @SerialName("experience_type") val experienceType: String,
    @SerialName("format_type") val formatType: String?,
    @SerialName("situation") val situation: String?,
    @SerialName("task") val task: String?,
    @SerialName("action") val action: String?,
    @SerialName("result") val result: String?,
    @SerialName("problem") val problem: String?,
    @SerialName("solution") val solution: String?,
    @SerialName("insight") val insight: String?,
    @SerialName("content") val content: String?,
    @SerialName("category") val category: String,
    @SerialName("tags") val tags: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)

fun ExperienceResponse.toExperience() = Experience(
    id = id,
    tags = tags.split(",").map { it.trim() },
    situation = resolvedSituation(),
    task = resolvedTask(),
    action = resolvedAction(),
    result = resolvedResult(),
    category = when(category) {
        "주도적 실행력" -> ExperienceCategory.PROACTIVE_EXECUTION
        "기술적 전문성" -> ExperienceCategory.TECHNICAL_EXPERTISE
        "논리적 분석력" -> ExperienceCategory.LOGICAL_ANALYSIS
        "창의적 문제해결" -> ExperienceCategory.CREATIVE_PROBLEM_SOLVING
        "협력적 소통" -> ExperienceCategory.COLLABORATIVE_COMMUNICATION
        "끈기 있는 책임감" -> ExperienceCategory.TENACIOUS_RESPONSIBILITY
        "유연한 적응력" -> ExperienceCategory.FLEXIBLE_ADAPTABILITY
        "고객 가치 지향" -> ExperienceCategory.CUSTOMER_VALUE_ORIENTATION
        else -> throw IllegalArgumentException("Invalid category: $category")
    },
    startDate = startDate.toLocalDate() ?: LocalDate.MIN,
    endDate = endDate.toLocalDate(),
    experienceType = ExperienceType.fromTypeName(experienceType) ?: ExperienceType.DefaultType,
    formatType = formatType,
    title = title
)

private fun ExperienceResponse.resolvedSituation(): String {
    return when (formatType?.trim()?.uppercase()) {
        "PSI" -> problem.orEmpty()
        "FREEFORM" -> content.orEmpty()
        else -> situation.orEmpty()
    }
}

private fun ExperienceResponse.resolvedTask(): String {
    return when (formatType?.trim()?.uppercase()) {
        "PSI" -> solution.orEmpty()
        "FREEFORM" -> ""
        else -> task.orEmpty()
    }
}

private fun ExperienceResponse.resolvedAction(): String {
    return when (formatType?.trim()?.uppercase()) {
        "PSI" -> insight.orEmpty()
        "FREEFORM" -> ""
        else -> action.orEmpty()
    }
}

private fun ExperienceResponse.resolvedResult(): String {
    return when (formatType?.trim()?.uppercase()) {
        "FREEFORM" -> ""
        else -> result.orEmpty()
    }
}

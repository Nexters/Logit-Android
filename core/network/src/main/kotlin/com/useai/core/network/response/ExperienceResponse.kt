package com.useai.core.network.response

import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperienceResponse(
    @SerialName("id") val id: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("user_id") val userId: String,
    @SerialName("situation") val situation: String,
    @SerialName("task") val task: String,
    @SerialName("action") val action: String,
    @SerialName("result") val result: String,
    @SerialName("category") val category: String,
    @SerialName("date") val date: String,
    @SerialName("experience_type") val experienceType: String,
    @SerialName("title") val title: String,
)

fun ExperienceResponse.toExperience() = Experience(
    id = id,
    tags = tags,
    situation = situation,
    task = task,
    action = action,
    result = result,
    category = when(category) {
        "주도적 실행력" -> ExperienceCategory.PROACTIVE_EXECUTION
        "기술적 전문성" -> ExperienceCategory.TECHNICAL_EXPERTISE
        "논리적 분석력" -> ExperienceCategory.LOGICAL_ANALYSIS
        "창의적 문제해결" -> ExperienceCategory.CREATIVE_PROBLEM_SOLVING
        "협업적 소통" -> ExperienceCategory.COLLABORATIVE_COMMUNICATION
        "끈기 있는 책임감" -> ExperienceCategory.TENACIOUS_RESPONSIBILITY
        "유연한 적응력" -> ExperienceCategory.FLEXIBLE_ADAPTABILITY
        "고객 가치 지향" -> ExperienceCategory.CUSTOMER_VALUE_ORIENTATION
        else -> throw IllegalArgumentException("Invalid category: $category")
    },
    date = date,
    experienceType = experienceType,
    title = title
)

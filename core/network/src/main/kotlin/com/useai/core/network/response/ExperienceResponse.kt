package com.useai.core.network.response

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

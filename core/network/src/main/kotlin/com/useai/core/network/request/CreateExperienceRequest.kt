package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateExperienceRequest(
    @SerialName("title") val title: String,
    @SerialName("experience_type") val experienceType: String,
    @SerialName("format_type") val formatType: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String? = null,
    @SerialName("tags") val tags: String,
    @SerialName("situation") val situation: String,
    @SerialName("task") val task: String,
    @SerialName("action") val action: String,
    @SerialName("result") val result: String,
)

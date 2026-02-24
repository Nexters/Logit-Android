package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateExperienceRequest(
    @SerialName("experience_type") val experienceType: String? = null,
    @SerialName("format_type") val formatType: String? = null,
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("end_date") val endDate: String? = null,
    @SerialName("tags") val tags: String? = null,
    @SerialName("situation") val situation: String? = null,
    @SerialName("task") val task: String? = null,
    @SerialName("action") val action: String? = null,
    @SerialName("result") val result: String? = null,
    @SerialName("problem") val problem: String? = null,
    @SerialName("solution") val solution: String? = null,
    @SerialName("insight") val insight: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("title") val title: String? = null
)

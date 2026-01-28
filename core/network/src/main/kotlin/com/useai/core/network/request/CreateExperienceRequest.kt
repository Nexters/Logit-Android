package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateExperienceRequest(
    @SerialName("category") val category: String,
    @SerialName("date") val date: String,
    @SerialName("experience_type") val experienceType: String,
    @SerialName("situation") val situation: String,
    @SerialName("task") val task: String,
    @SerialName("action") val action: String,
    @SerialName("result") val result: String,
    @SerialName("title") val title: String
)

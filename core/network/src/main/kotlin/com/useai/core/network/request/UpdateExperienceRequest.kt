package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateExperienceRequest(
    @SerialName("situation") val situation: String? = null,
    @SerialName("task") val task: String? = null,
    @SerialName("action") val action: String? = null,
    @SerialName("result") val result: String? = null,
    @SerialName("title") val title: String? = null
)

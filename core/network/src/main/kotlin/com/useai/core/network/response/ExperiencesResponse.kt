package com.useai.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperiencesResponse(
    @SerialName("experiences") val experiences: List<ExperienceResponse>,
    @SerialName("limit") val limit: Int,
    @SerialName("offset") val offset: Int,
    @SerialName("total") val total: Int
)

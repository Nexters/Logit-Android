package com.useai.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchingExperiencesResponse(
    @SerialName("total") val total: Int,
    @SerialName("results") val results: List<MatchingExperienceResponse>
)

@Serializable
data class MatchingExperienceResponse(
    @SerialName("experience") val experience: ExperienceResponse,
    @SerialName("score") val score: Float
)

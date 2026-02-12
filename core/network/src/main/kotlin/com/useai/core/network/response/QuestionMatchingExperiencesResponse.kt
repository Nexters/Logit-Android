package com.useai.core.network.response

import com.useai.core.model.experience.MatchingExperience
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionMatchingExperiencesResponse(
    @SerialName("total") val total: Int,
    @SerialName("experiences") val experiences: List<QuestionMatchingExperienceResponse>
)

@Serializable
data class QuestionMatchingExperienceResponse(
    @SerialName("experience") val experience: ExperienceResponse,
    @SerialName("similarity_score") val similarityScore: Float
)

fun QuestionMatchingExperienceResponse.toMatchingExperience() = MatchingExperience(
    experience = experience.toExperience(),
    score = similarityScore
)

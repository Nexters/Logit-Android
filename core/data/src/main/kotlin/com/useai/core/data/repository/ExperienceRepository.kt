package com.useai.core.data.repository

import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceParam
import com.useai.core.model.experience.MatchingExperience
import com.useai.core.network.request.UpdateExperienceRequest

interface ExperienceRepository {

    suspend fun createExperience(experience: ExperienceParam): Result<Experience>
    suspend fun getExperiences(): Result<List<Experience>>
    suspend fun getExperience(experienceId: String): Result<Experience>
    suspend fun searchExperience(query: String): Result<List<MatchingExperience>>
    suspend fun updateExperience(experienceId: String, request: UpdateExperienceRequest): Result<Experience>
    suspend fun deleteExperience(experienceId: String): Result<Unit>
}

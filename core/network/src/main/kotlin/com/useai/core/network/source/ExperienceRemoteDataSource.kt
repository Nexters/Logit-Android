package com.useai.core.network.source

import com.useai.core.network.request.CreateExperienceRequest
import com.useai.core.network.request.UpdateExperienceRequest
import com.useai.core.network.response.ExperienceResponse
import com.useai.core.network.response.ExperiencesResponse
import com.useai.core.network.response.MatchingExperiencesResponse

interface ExperienceRemoteDataSource {

    suspend fun createExperience(request: CreateExperienceRequest): ExperienceResponse
    suspend fun getExperiences(): ExperiencesResponse
    suspend fun getExperience(experienceId: String): ExperienceResponse
    suspend fun searchExperience(query: String): MatchingExperiencesResponse
    suspend fun updateExperience(experienceId: String, request: UpdateExperienceRequest): ExperienceResponse
    suspend fun deleteExperience(experienceId: String)
}

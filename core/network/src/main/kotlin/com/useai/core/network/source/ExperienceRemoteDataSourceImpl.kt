package com.useai.core.network.source

import com.useai.core.network.api.ExperienceApi
import com.useai.core.network.request.CreateExperienceRequest
import com.useai.core.network.request.UpdateExperienceRequest
import com.useai.core.network.response.ExperienceResponse
import com.useai.core.network.response.ExperiencesResponse
import com.useai.core.network.response.MatchingExperiencesResponse
import javax.inject.Inject

internal class ExperienceRemoteDataSourceImpl @Inject constructor(
    private val experienceApi: ExperienceApi
) : ExperienceRemoteDataSource {

    override suspend fun createExperience(request: CreateExperienceRequest): ExperienceResponse {
        return experienceApi.createExperience(request)
    }

    override suspend fun getExperiences(): ExperiencesResponse {
        return experienceApi.getExperiences()
    }

    override suspend fun getExperience(experienceId: String): ExperienceResponse {
        return experienceApi.getExperience(experienceId)
    }

    override suspend fun searchExperience(query: String): MatchingExperiencesResponse {
        return experienceApi.searchExperience(query)
    }

    override suspend fun updateExperience(
        experienceId: String,
        request: UpdateExperienceRequest
    ): ExperienceResponse {
        return experienceApi.updateExperience(experienceId, request)
    }

    override suspend fun deleteExperience(experienceId: String) {
        experienceApi.deleteExperience(experienceId)
    }
}

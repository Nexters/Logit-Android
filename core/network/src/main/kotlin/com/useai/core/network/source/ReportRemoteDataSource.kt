package com.useai.core.network.source

import com.useai.core.network.response.ExperienceCategoryCountResponse
import com.useai.core.network.response.ExperienceSummaryResponse
import com.useai.core.network.response.ExperienceTagCountResponse
import com.useai.core.network.response.ExperienceTypeCountResponse

interface ReportRemoteDataSource {

    suspend fun getExperienceSummary(): ExperienceSummaryResponse
    suspend fun getExperienceTypeCount(): ExperienceTypeCountResponse
    suspend fun getExperienceCategoryCount(): ExperienceCategoryCountResponse
    suspend fun getExperienceTagCount(): ExperienceTagCountResponse
}

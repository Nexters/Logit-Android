package com.useai.core.network.source

import com.useai.core.network.api.ReportApi
import com.useai.core.network.response.ExperienceCategoryCountResponse
import com.useai.core.network.response.ExperienceSummaryResponse
import com.useai.core.network.response.ExperienceTagCountResponse
import com.useai.core.network.response.ExperienceTypeCountResponse
import javax.inject.Inject

internal class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportRemoteDataSource {

    override suspend fun getExperienceSummary(): ExperienceSummaryResponse {
        return reportApi.getExperienceSummary()
    }

    override suspend fun getExperienceTypeCount(): ExperienceTypeCountResponse {
        return reportApi.getExperienceTypeCount()
    }

    override suspend fun getExperienceCategoryCount(): ExperienceCategoryCountResponse {
        return reportApi.getExperienceCategoryCount()
    }

    override suspend fun getExperienceTagCount(): ExperienceTagCountResponse {
        return reportApi.getExperienceTagCount()
    }
}

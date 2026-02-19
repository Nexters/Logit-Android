package com.useai.core.network.api

import com.useai.core.network.response.ExperienceCategoryCountResponse
import com.useai.core.network.response.ExperienceSummaryResponse
import com.useai.core.network.response.ExperienceTagCountResponse
import com.useai.core.network.response.ExperienceTypeCountResponse
import retrofit2.http.GET

interface ReportApi {

    @GET("api/v1/report/experience-summary")
    suspend fun getExperienceSummary(): ExperienceSummaryResponse

    @GET("api/v1/report/experience-type-count")
    suspend fun getExperienceTypeCount(): ExperienceTypeCountResponse

    @GET("api/v1/report/experience-category-count")
    suspend fun getExperienceCategoryCount(): ExperienceCategoryCountResponse

    @GET("api/v1/report/experience-tag-count")
    suspend fun getExperienceTagCount(): ExperienceTagCountResponse
}

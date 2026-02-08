package com.useai.core.network.api

import androidx.annotation.IntRange
import com.useai.core.network.request.CreateExperienceRequest
import com.useai.core.network.request.UpdateExperienceRequest
import com.useai.core.network.response.ExperienceResponse
import com.useai.core.network.response.ExperiencesResponse
import com.useai.core.network.response.MatchingExperiencesResponse
import com.useai.core.network.response.QuestionMatchingExperiencesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ExperienceApi {

    @POST("api/v1/experiences")
    suspend fun createExperience(
        @Body request: CreateExperienceRequest
    ): ExperienceResponse

    @GET("api/v1/experiences")
    suspend fun getExperiences(
        @IntRange(1,1000) @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): ExperiencesResponse

    @GET("api/v1/experiences/search")
    suspend fun searchExperience(
        @Query("q") query: String,
        @IntRange(1,100) @Query("limit") limit: Int = 100,
    ): MatchingExperiencesResponse

    @GET("api/v1/experiences/match-question/{question_id}")
    suspend fun getMatchingExperiences(
        @Path("question_id") questionId: String
    ): QuestionMatchingExperiencesResponse

    @GET("api/v1/experiences/{experience_id}")
    suspend fun getExperience(
        @Path("experience_id") experienceId: String
    ): ExperienceResponse

    @GET("api/v1/experiences/{experience_id}")
    suspend fun updateExperience(
        @Path("experience_id") experienceId: String,
        @Body request: UpdateExperienceRequest
    ): ExperienceResponse

    @DELETE("api/v1/experiences/{experience_id}")
    suspend fun deleteExperience(
        @Path("experience_id") experienceId: String
    )


}

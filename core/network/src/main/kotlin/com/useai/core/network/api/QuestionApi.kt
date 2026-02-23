package com.useai.core.network.api

import com.useai.core.network.request.CreateQuestionRequest
import com.useai.core.network.request.UpdateQuestionRequest
import com.useai.core.network.response.QuestionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestionApi {

    @POST("api/v1/projects/{project_id}/questions/")
    suspend fun createQuestion(
        @Path("project_id") projectId: String,
        @Body request: CreateQuestionRequest
    ) : QuestionResponse

    @GET("api/v1/projects/{project_id}/questions/")
    suspend fun getQuestions(
        @Path("project_id") projectId: String
    ) : List<QuestionResponse>

    @GET("api/v1/projects/{project_id}/questions/{questions_id}/")
    suspend fun getQuestion(
        @Path("project_id") projectId: String,
        @Path("questions_id") questionId: String
    ) : QuestionResponse

    @PATCH("api/v1/projects/{project_id}/questions/{questions_id}")
    suspend fun updateQuestion(
        @Path("project_id") projectId: String,
        @Path("questions_id") questionId: String,
        @Body request: UpdateQuestionRequest
    ) : QuestionResponse

    @DELETE("api/v1/projects/{project_id}/questions/{questions_id}")
    suspend fun deleteQuestion(
        @Path("project_id") projectId: String,
        @Path("questions_id") questionId: String
    )
}

package com.useai.core.network.api

import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.request.UpdateProjectRequest
import com.useai.core.network.response.ProjectListItemResponse
import com.useai.core.network.response.ProjectResponse
import com.useai.core.network.response.ProjectWithQuestionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {

    @POST("api/v1/projects/")
    suspend fun createProject(
        @Body request: CreateProjectRequest
    ): ProjectWithQuestionResponse

    @GET("api/v1/projects/")
    suspend fun getProjects(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<ProjectListItemResponse>

    @GET("api/v1/projects/{project_id}")
    suspend fun getProject(
        @Path("project_id") projectId: String
    ): ProjectResponse

    @PATCH("api/v1/projects/{project_id}")
    suspend fun updateProject(
        @Path("project_id") projectId: String,
        @Body request: UpdateProjectRequest
    ): ProjectWithQuestionResponse

    @DELETE("api/v1/projects/{project_id}")
    suspend fun deleteProject(
        @Path("project_id") projectId: String
    )
}

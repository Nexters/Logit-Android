package com.useai.core.network.api

import com.useai.core.network.request.NewProjectRequest
import com.useai.core.network.response.NewProjectResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ProjectsApi {
    @POST("/api/v1/projects/")
    suspend fun createNewProject(
        @Body request: NewProjectRequest,
    ): NewProjectResponse
}

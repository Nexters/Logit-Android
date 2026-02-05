package com.useai.core.network.api

import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.response.ProjectResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {

    @POST("api/v1/projects/")
    suspend fun createProject(
        @Body request: CreateProjectRequest
    ): ProjectResponse
}

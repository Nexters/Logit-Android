package com.useai.core.network.source

import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.response.ProjectResponse

interface ProjectRemoteDataSource {
    suspend fun createProject(request: CreateProjectRequest): ProjectResponse
}

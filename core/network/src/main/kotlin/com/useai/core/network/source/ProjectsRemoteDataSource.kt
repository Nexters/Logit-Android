package com.useai.core.network.source

import com.useai.core.network.request.NewProjectRequest
import com.useai.core.network.response.NewProjectResponse

interface ProjectsRemoteDataSource {
    suspend fun createNewProject(request: NewProjectRequest): NewProjectResponse
}

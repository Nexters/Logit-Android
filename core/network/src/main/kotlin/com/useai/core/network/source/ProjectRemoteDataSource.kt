package com.useai.core.network.source

import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.request.UpdateProjectRequest
import com.useai.core.network.response.ProjectListItemResponse
import com.useai.core.network.response.ProjectResponse

interface ProjectRemoteDataSource {
    suspend fun createProject(request: CreateProjectRequest): ProjectResponse
    suspend fun getProjects(): List<ProjectListItemResponse>
    suspend fun getProject(projectId: String): ProjectResponse
    suspend fun updateProject(projectId: String, request: UpdateProjectRequest): ProjectResponse
}

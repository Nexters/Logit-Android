package com.useai.core.network.source

import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.request.UpdateProjectRequest
import com.useai.core.network.response.ProjectListItemResponse
import com.useai.core.network.response.ProjectWithQuestionResponse

interface ProjectRemoteDataSource {
    suspend fun createProject(request: CreateProjectRequest): ProjectWithQuestionResponse
    suspend fun getProjects(): List<ProjectListItemResponse>
    suspend fun getProject(projectId: String): ProjectWithQuestionResponse
    suspend fun updateProject(projectId: String, request: UpdateProjectRequest): ProjectWithQuestionResponse
    suspend fun deleteProject(projectId: String)
}

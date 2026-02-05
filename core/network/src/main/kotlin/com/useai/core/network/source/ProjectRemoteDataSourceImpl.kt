package com.useai.core.network.source

import com.useai.core.network.api.ProjectApi
import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.response.ProjectListItemResponse
import com.useai.core.network.response.ProjectResponse
import javax.inject.Inject

internal class ProjectRemoteDataSourceImpl @Inject constructor(
    private val projectApi: ProjectApi
) : ProjectRemoteDataSource {

    override suspend fun createProject(request: CreateProjectRequest): ProjectResponse {
        return projectApi.createProject(request)
    }

    override suspend fun getProjects(): List<ProjectListItemResponse> {
        return projectApi.getProjects()
    }

    override suspend fun getProject(projectId: String): ProjectResponse {
        return projectApi.getProject(projectId)
    }
}

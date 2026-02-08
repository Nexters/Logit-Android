package com.useai.core.network.source

import com.useai.core.network.api.ProjectsApi
import com.useai.core.network.request.NewProjectRequest
import com.useai.core.network.response.NewProjectResponse
import javax.inject.Inject

class ProjectsRemoteDataSourceImpl @Inject constructor(
    private val projectsApi: ProjectsApi,
) : ProjectsRemoteDataSource {
    override suspend fun createNewProject(request: NewProjectRequest): NewProjectResponse {
        return projectsApi.createNewProject(request)
    }
}

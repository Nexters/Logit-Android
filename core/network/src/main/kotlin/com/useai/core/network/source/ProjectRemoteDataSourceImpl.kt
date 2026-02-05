package com.useai.core.network.source

import com.useai.core.network.api.ProjectApi
import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.response.ProjectResponse
import javax.inject.Inject

internal class ProjectRemoteDataSourceImpl @Inject constructor(
    private val projectApi: ProjectApi
) : ProjectRemoteDataSource {

    override suspend fun createProject(request: CreateProjectRequest): ProjectResponse {
        return projectApi.createProject(request)
    }
}

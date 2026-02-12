package com.useai.core.data.repository

import com.useai.core.model.project.Project
import com.useai.core.model.project.ProjectListItem
import com.useai.core.model.project.ProjectParam
import com.useai.core.model.project.UpdateProjectParam

interface ProjectRepository {
    suspend fun createProject(projectParam: ProjectParam): Result<Project>
    suspend fun getProjects(): Result<List<ProjectListItem>>
    suspend fun getProject(projectId: String): Result<Project>
    suspend fun updateProject(projectId: String, updateProjectParam: UpdateProjectParam): Result<Project>
    suspend fun deleteProject(projectId: String): Result<Unit>
}

package com.useai.core.data.repository

import com.useai.core.model.project.Project
import com.useai.core.model.project.ProjectParam

interface ProjectRepository {
    suspend fun createProject(projectParam: ProjectParam): Result<Project>
}

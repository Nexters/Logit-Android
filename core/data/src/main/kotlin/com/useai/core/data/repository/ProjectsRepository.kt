package com.useai.core.data.repository

import com.useai.core.model.project.NewProject
import com.useai.core.model.project.Project

interface ProjectsRepository {
    suspend fun createNewProject(newProject: NewProject): Result<Project>
}

package com.useai.core.data.repository

import com.useai.core.common.extensions.toFormattedString
import com.useai.core.model.project.Project
import com.useai.core.model.project.ProjectListItem
import com.useai.core.model.project.ProjectParam
import com.useai.core.model.project.UpdateProjectParam
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.request.ProjectQuestionRequest
import com.useai.core.network.request.UpdateProjectRequest
import com.useai.core.network.response.toProject
import com.useai.core.network.response.toProjectListItem
import com.useai.core.network.source.ProjectRemoteDataSource
import javax.inject.Inject

internal class ProjectRepositoryImpl @Inject constructor(
    private val projectRemoteDataSource: ProjectRemoteDataSource
) : ProjectRepository {

    override suspend fun createProject(projectParam: ProjectParam): Result<Project> {
        return runCatchingWith {
            projectRemoteDataSource.createProject(
                CreateProjectRequest(
                    company = projectParam.company,
                    companyTalent = projectParam.companyTalent,
                    dueDate = projectParam.dueDate.toFormattedString().orEmpty(),
                    jobPosition = projectParam.jobPosition,
                    recruitNotice = projectParam.recruitNotice,
                    questions = projectParam.questions.map {
                        ProjectQuestionRequest(
                            maxLength = it.maxLength,
                            question = it.question
                        )
                    }
                )
            ).toProject()
        }
    }

    override suspend fun getProjects(): Result<List<ProjectListItem>> {
        return runCatchingWith {
            projectRemoteDataSource.getProjects().map { it.toProjectListItem() }
        }
    }

    override suspend fun getProject(projectId: String): Result<Project> {
        return runCatchingWith {
            projectRemoteDataSource.getProject(projectId).toProject()
        }
    }

    override suspend fun updateProject(
        projectId: String,
        updateProjectParam: UpdateProjectParam
    ): Result<Project> {
        return runCatchingWith {
            projectRemoteDataSource.updateProject(
                projectId,
                UpdateProjectRequest(
                    company = updateProjectParam.company,
                    companyTalent = updateProjectParam.companyTalent,
                    dueDate = updateProjectParam.dueDate?.toFormattedString(),
                    jobPosition = updateProjectParam.jobPosition
                )
            ).toProject()
        }
    }

    override suspend fun deleteProject(projectId: String): Result<Unit> {
        return runCatchingWith {
            projectRemoteDataSource.deleteProject(projectId)
        }
    }
}

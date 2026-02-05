package com.useai.core.data.repository

import com.useai.core.common.extensions.toFormattedString
import com.useai.core.model.project.Project
import com.useai.core.model.project.ProjectParam
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.request.CreateProjectRequest
import com.useai.core.network.request.ProjectQuestionRequest
import com.useai.core.network.response.toProject
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
}

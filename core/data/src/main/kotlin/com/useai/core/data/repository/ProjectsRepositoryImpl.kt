package com.useai.core.data.repository

import com.useai.core.model.project.NewProject
import com.useai.core.model.project.Project
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.request.NewProjectRequest
import com.useai.core.network.request.Question
import com.useai.core.network.response.toProject
import com.useai.core.network.source.ProjectsRemoteDataSource
import javax.inject.Inject

class ProjectsRepositoryImpl @Inject constructor(
    private val projectsRemoteDataSource: ProjectsRemoteDataSource,
) : ProjectsRepository {
    override suspend fun createNewProject(newProject: NewProject): Result<Project> {
        return runCatchingWith {
            projectsRemoteDataSource.createNewProject(
                request = NewProjectRequest(
                    company = newProject.companyName,
                    jobName = newProject.jobName,
                    jobDesc = newProject.jobDesc,
                    talent = newProject.talent,
                    questions = newProject.questions.map {
                        Question(
                            maxLength = it.maxLength,
                            question = it.question
                        )
                    },
                    dueDate = newProject.dueDate,
                )
            ).toProject()
        }
    }
}

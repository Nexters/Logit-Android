package com.useai.core.data.repository

import com.useai.core.model.chat.Question
import com.useai.core.model.project.ProjectQuestionParam
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.request.CreateQuestionRequest
import com.useai.core.network.request.UpdateQuestionRequest
import com.useai.core.network.response.toQuestion
import com.useai.core.network.source.QuestionRemoteDataSource
import javax.inject.Inject

internal class QuestionRepositoryImpl @Inject constructor(
    private val questionRemoteDataSource: QuestionRemoteDataSource
) : QuestionRepository {

    override suspend fun createQuestion(projectId: String, question: ProjectQuestionParam): Result<String> {
        return runCatchingWith {
            questionRemoteDataSource.createQuestion(
                projectId,
                CreateQuestionRequest(
                    title = question.question,
                    maxLength = question.maxLength
                )
            ).id
        }
    }

    override suspend fun getQuestions(projectId: String): Result<List<Question>> {
        return runCatchingWith {
            questionRemoteDataSource.getQuestions(projectId).map { it.toQuestion() }
        }
    }

    override suspend fun getQuestion(projectId: String, questionId: String): Result<Question> {
        return runCatchingWith {
            questionRemoteDataSource.getQuestion(projectId, questionId).toQuestion()
        }
    }

    override suspend fun updateQuestion(projectId: String, question: Question): Result<Question> {
        return runCatchingWith {
            questionRemoteDataSource.updateQuestion(
                projectId,
                question.id,
                UpdateQuestionRequest(
                    title = question.title,
                    maxLength = question.maxLength,
                    letter = question.letter
                )
            ).toQuestion()
        }
    }

    override suspend fun deleteQuestion(projectId: String, questionId: String): Result<Unit> {
        return runCatchingWith {
            questionRemoteDataSource.deleteQuestion(projectId, questionId)
        }
    }
}

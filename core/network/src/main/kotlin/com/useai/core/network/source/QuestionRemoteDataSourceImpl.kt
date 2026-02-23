package com.useai.core.network.source

import com.useai.core.network.api.QuestionApi
import com.useai.core.network.request.CreateQuestionRequest
import com.useai.core.network.request.UpdateQuestionRequest
import com.useai.core.network.response.QuestionResponse
import javax.inject.Inject

internal class QuestionRemoteDataSourceImpl @Inject constructor(
    private val questionApi: QuestionApi
) : QuestionRemoteDataSource {

    override suspend fun createQuestion(projectId: String, request: CreateQuestionRequest): QuestionResponse {
        return questionApi.createQuestion(projectId, request)
    }

    override suspend fun getQuestions(projectId: String): List<QuestionResponse> {
        return questionApi.getQuestions(projectId)
    }

    override suspend fun getQuestion(
        projectId: String,
        questionId: String
    ): QuestionResponse {
        return questionApi.getQuestion(projectId, questionId)
    }

    override suspend fun updateQuestion(
        projectId: String,
        questionId: String,
        request: UpdateQuestionRequest
    ): QuestionResponse {
        return questionApi.updateQuestion(projectId, questionId, request)
    }

    override suspend fun completeQuestion(projectId: String, questionId: String): QuestionResponse {
        return questionApi.completeQuestion(projectId, questionId)
    }

    override suspend fun deleteQuestion(projectId: String, questionId: String) {
        questionApi.deleteQuestion(projectId, questionId)
    }
}

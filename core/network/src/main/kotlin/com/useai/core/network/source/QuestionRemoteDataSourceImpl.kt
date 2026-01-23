package com.useai.core.network.source

import com.useai.core.network.api.QuestionApi
import com.useai.core.network.response.CreateQuestionResponse
import com.useai.core.network.response.QuestionResponse
import javax.inject.Inject

internal class QuestionRemoteDataSourceImpl @Inject constructor(
    private val questionApi: QuestionApi
) : QuestionRemoteDataSource {

    override suspend fun createQuestion(projectId: String): CreateQuestionResponse {
        return questionApi.createQuestion(projectId)
    }

    override suspend fun getQuestions(projectId: String): List<QuestionResponse> {
        return questionApi.getQuestions(projectId)
    }

    override suspend fun getQuestionDetail(
        projectId: String,
        questionId: String
    ): QuestionResponse {
        return questionApi.getQuestionDetail(projectId, questionId)
    }

    override suspend fun updateQuestion(
        projectId: String,
        questionId: String
    ): QuestionResponse {
        return questionApi.updateQuestion(projectId, questionId)
    }

    override suspend fun deleteQuestion(projectId: String, questionId: String) {
        questionApi.deleteQuestion(projectId, questionId)
    }
}

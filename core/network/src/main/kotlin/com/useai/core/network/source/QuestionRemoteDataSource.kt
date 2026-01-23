package com.useai.core.network.source

import com.useai.core.network.response.CreateQuestionResponse
import com.useai.core.network.response.QuestionResponse

interface QuestionRemoteDataSource {

    suspend fun createQuestion(projectId: String): CreateQuestionResponse
    suspend fun getQuestions(projectId: String): List<QuestionResponse>
    suspend fun getQuestion(projectId: String, questionId: String): QuestionResponse
    suspend fun updateQuestion(projectId: String, questionId: String): QuestionResponse
    suspend fun deleteQuestion(projectId: String, questionId: String)
}

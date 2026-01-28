package com.useai.core.data.repository

import com.useai.core.model.chat.Question

interface QuestionRepository {

    suspend fun createQuestion(projectId: String, question: Question): Result<String>
    suspend fun getQuestions(projectId: String): Result<List<Question>>
    suspend fun getQuestion(projectId: String, questionId: String): Result<Question>
    suspend fun updateQuestion(projectId: String, question: Question): Result<Question>
    suspend fun deleteQuestion(projectId: String, questionId: String): Result<Unit>
}

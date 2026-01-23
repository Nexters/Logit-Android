package com.useai.core.network.source

import com.useai.core.network.response.ChattingHistoryResponse

interface ChattingRemoteDataSource {

    suspend fun getChatHistory(projectId: Int, questionId: Int): ChattingHistoryResponse
}

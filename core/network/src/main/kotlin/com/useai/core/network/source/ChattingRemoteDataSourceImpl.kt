package com.useai.core.network.source

import com.useai.core.network.api.ChattingApi
import com.useai.core.network.response.ChattingHistoryResponse
import javax.inject.Inject

internal class ChattingRemoteDataSourceImpl @Inject constructor(
    private val chattingApi: ChattingApi
) : ChattingRemoteDataSource {

    override suspend fun getChatHistory(projectId: Int, questionId: Int): ChattingHistoryResponse {
        return chattingApi.getChattingHistory(projectId)
    }
}

package com.useai.core.network.source

import com.useai.core.network.request.StartChattingStreamRequest
import com.useai.core.network.request.UpdateLetterRequest
import com.useai.core.network.response.ChattingHistoryResponse
import com.useai.core.network.response.ChattingStreamingResponse
import kotlinx.coroutines.flow.Flow

interface ChattingRemoteDataSource {

    fun startChattingStream(request: StartChattingStreamRequest): Flow<ChattingStreamingResponse>
    suspend fun getChatHistory(projectId: Int, questionId: Int): ChattingHistoryResponse
    suspend fun updateLetter(chattingId: String, request: UpdateLetterRequest)
}

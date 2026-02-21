package com.useai.core.data.repository

import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.ChattingStreaming
import com.useai.core.network.error.runCatchingWith
import com.useai.core.network.request.StartChattingStreamRequest
import com.useai.core.network.request.UpdateLetterRequest
import com.useai.core.network.response.toChattingHistory
import com.useai.core.network.response.toChattingStreaming
import com.useai.core.network.source.ChattingRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class ChattingRepositoryImpl @Inject constructor(
    private val chattingRemoteDataSource: ChattingRemoteDataSource
) : ChattingRepository {

    override fun startChattingStream(
        questionId: String,
        sendingMessage: String,
        experienceIds: List<String>
    ): Flow<ChattingStreaming> {
        return chattingRemoteDataSource.startChattingStream(
            StartChattingStreamRequest(
                sendingMessage = sendingMessage,
                questionId = questionId,
                experienceIds = experienceIds
            )
        ).mapNotNull { it.toChattingStreaming() }
    }

    override suspend fun getChatHistory(questionId: String): Result<ChattingHistory> {
        return runCatchingWith {
            chattingRemoteDataSource.getChatHistory(questionId).toChattingHistory()
        }
    }

    override suspend fun updateLetter(
        chattingId: String,
        questionId: String,
        content: String
    ): Result<Unit> {
        return runCatchingWith {
            chattingRemoteDataSource.updateLetter(
                chattingId = chattingId,
                request = UpdateLetterRequest(
                    questionId = questionId,
                    content = content
                )
            )
        }
    }
}

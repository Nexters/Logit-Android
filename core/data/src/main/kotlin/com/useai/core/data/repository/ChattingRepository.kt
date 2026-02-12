package com.useai.core.data.repository

import com.useai.core.model.chat.ChattingHistory
import com.useai.core.model.chat.ChattingStreaming
import kotlinx.coroutines.flow.Flow

interface ChattingRepository {

    fun startChattingStream(
        questionId: String,
        sendingMessage: String,
        experienceIds: List<String>
    ): Flow<ChattingStreaming>
    suspend fun getChatHistory(questionId: String): Result<ChattingHistory>
    suspend fun updateLetter(chattingId: String, questionId: String, content: String): Result<Unit>
}

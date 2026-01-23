package com.useai.core.network.api

import com.useai.core.network.request.UpdateLetterRequest
import com.useai.core.network.response.ChattingHistoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ChattingApi {

    @GET("api/v1/projects/chats/{question_id}")
    suspend fun getChattingHistory(
        @Path("question_id") questionId: String
    ): ChattingHistoryResponse

    @PATCH("api/v1/projects/chats/{chat_id}/answer")
    suspend fun updateLetter(
        @Path("chat_id") chatId: String,
        @Body updateLetterRequest: UpdateLetterRequest
    )
}

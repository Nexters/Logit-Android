package com.useai.core.network.api

import com.useai.core.network.response.ChattingHistoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChattingApi {

    @GET("api/v1/projects/chats/{question_id}")
    suspend fun getChattingHistory(
        @Path("question_id") questionId: Int
    ): ChattingHistoryResponse
}

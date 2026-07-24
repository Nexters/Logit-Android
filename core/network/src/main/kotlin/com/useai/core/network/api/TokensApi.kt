package com.useai.core.network.api

import com.useai.core.network.response.TokenBalanceResponse
import retrofit2.http.GET

interface TokensApi {
    @GET("/api/v1/tokens/balance")
    suspend fun getTokenBalance(): TokenBalanceResponse
}

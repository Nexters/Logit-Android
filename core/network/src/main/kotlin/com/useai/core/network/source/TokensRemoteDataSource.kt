package com.useai.core.network.source

import com.useai.core.network.response.TokenBalanceResponse

interface TokensRemoteDataSource {
    suspend fun getTokenBalance(): TokenBalanceResponse
}

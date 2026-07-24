package com.useai.core.network.source

import com.useai.core.network.api.TokensApi
import com.useai.core.network.response.TokenBalanceResponse
import javax.inject.Inject

internal class TokensRemoteDataSourceImpl @Inject constructor(
    private val tokensApi: TokensApi,
) : TokensRemoteDataSource {
    override suspend fun getTokenBalance(): TokenBalanceResponse {
        return tokensApi.getTokenBalance()
    }
}

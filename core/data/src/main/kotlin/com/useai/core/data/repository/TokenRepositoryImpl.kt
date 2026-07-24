package com.useai.core.data.repository

import com.useai.core.network.source.TokensRemoteDataSource
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
    private val tokensRemoteDataSource: TokensRemoteDataSource,
) : TokenRepository {
    override suspend fun getTokenBalance(): Result<Int> = runCatching {
        tokensRemoteDataSource.getTokenBalance().balance
    }
}

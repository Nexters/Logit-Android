package com.useai.core.data.repository

import com.useai.core.model.account.TokenBalance
import com.useai.core.network.response.toTokenBalance
import com.useai.core.network.response.toTokenGrant
import com.useai.core.network.source.TokensRemoteDataSource
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
    private val tokensRemoteDataSource: TokensRemoteDataSource,
    private val tokenGrantEventBus: TokenGrantEventBus,
) : TokenRepository {
    override val tokenGrants = tokenGrantEventBus.tokenGrants

    override suspend fun getTokenBalance(): Result<TokenBalance> = runCatching {
        tokensRemoteDataSource.getTokenBalance().also { response ->
            tokenGrantEventBus.emit(response.toTokenGrant())
        }.toTokenBalance()
    }
}

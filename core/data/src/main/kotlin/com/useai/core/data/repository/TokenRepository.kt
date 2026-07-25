package com.useai.core.data.repository

import com.useai.core.model.account.TokenBalance
import com.useai.core.model.account.TokenGrant
import kotlinx.coroutines.flow.SharedFlow

interface TokenRepository {
    val tokenGrants: SharedFlow<TokenGrant>

    suspend fun getTokenBalance(): Result<TokenBalance>
}

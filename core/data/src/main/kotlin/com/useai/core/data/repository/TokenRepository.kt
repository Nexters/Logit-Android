package com.useai.core.data.repository

interface TokenRepository {
    suspend fun getTokenBalance(): Result<Int>
}

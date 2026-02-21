package com.useai.core.data.repository

import com.useai.core.model.account.Login

interface AccountRepository {
    suspend fun requestGoogleLogin(idToken: String): Result<Login>

    suspend fun setAccessToken(token: String): Result<Unit>

    suspend fun clear(): Result<Unit>
}

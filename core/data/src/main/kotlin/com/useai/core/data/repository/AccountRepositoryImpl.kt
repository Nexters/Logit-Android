package com.useai.core.data.repository

import com.useai.core.datastore.LogitPreferencesDataSource
import com.useai.core.model.account.Login
import com.useai.core.network.request.GoogleLoginRequest
import com.useai.core.network.source.AuthRemoteDataSource
import toLogin
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val logitPreferencesDataSource: LogitPreferencesDataSource,
) : AccountRepository {
    override suspend fun requestLogin(idToken: String): Result<Login> {
        return runCatching {
            authRemoteDataSource.requestGoogleLogin(
                GoogleLoginRequest(
                    idToken = idToken
                )
            ).toLogin()
        }
    }

    override suspend fun setAccessToken(token: String): Result<Unit> {
        logitPreferencesDataSource.setAccessToken(token)
        return Result.success(Unit)
    }

    override suspend fun clear(): Result<Unit> {
        logitPreferencesDataSource.clear()
        return Result.success(Unit)
    }
}

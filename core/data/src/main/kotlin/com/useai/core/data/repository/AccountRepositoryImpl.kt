package com.useai.core.data.repository

import com.useai.core.datastore.LogitPreferencesDataSource
import com.useai.core.model.account.Login
import com.useai.core.model.account.User
import com.useai.core.network.request.GoogleLoginRequest
import com.useai.core.network.request.UpdateUserRequest
import com.useai.core.network.response.toUser
import com.useai.core.network.source.AuthRemoteDataSource
import com.useai.core.network.source.UsersRemoteDataSource
import kotlinx.coroutines.flow.firstOrNull
import toLogin
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val logitPreferencesDataSource: LogitPreferencesDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
) : AccountRepository {
    override suspend fun getAccessToken(): String? {
        return logitPreferencesDataSource.accessToken.firstOrNull()
    }

    override suspend fun isLoggedIn(): Boolean {
        return getUser().isSuccess
    }

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
        return runCatching {
            logitPreferencesDataSource.setAccessToken(token)
        }
    }

    override suspend fun setRefreshToken(token: String): Result<Unit> {
        return runCatching {
            logitPreferencesDataSource.setRefreshToken(token)
        }
    }

    override suspend fun getUser(): Result<User> {
        return runCatching {
            usersRemoteDataSource.getUser().toUser()
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return runCatching {
            usersRemoteDataSource.getUserById(userId).toUser()
        }
    }

    override suspend fun updateUser(
        email: String?,
        fullName: String?
    ): Result<User> {
        return runCatching {
            usersRemoteDataSource.updateUser(
                UpdateUserRequest(
                    email = email,
                    fullName = fullName,
                )
            ).toUser()
        }
    }

    override suspend fun clear(): Result<Unit> {
        return runCatching {
            logitPreferencesDataSource.clear()
        }
    }

    override suspend fun requestLogout(): Result<Unit> {
        return runCatching {
            authRemoteDataSource.requestGoogleLogout()
        }
    }


    override suspend fun deleteUser(): Result<Unit> {
        return runCatching {
            usersRemoteDataSource.deleteUser()
        }
    }
}

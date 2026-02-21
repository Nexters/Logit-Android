package com.useai.core.data.repository

import com.useai.core.model.account.Login
import com.useai.core.model.account.User

interface AccountRepository {
    suspend fun requestLogin(idToken: String): Result<Login>

    suspend fun setAccessToken(token: String): Result<Unit>

    suspend fun refreshAccessToken(): Result<String>

    suspend fun getUser(): Result<User>

    suspend fun getUserById(userId: String): Result<User>

    suspend fun updateUser(
        email: String? = null,
        fullName: String? = null,
    ): Result<User>

    suspend fun clear(): Result<Unit>

    suspend fun requestLogout(): Result<Unit>

    suspend fun deleteUser(): Result<Unit>
}

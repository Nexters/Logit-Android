package com.useai.core.network.source

import com.useai.core.network.api.UsersApi
import com.useai.core.network.request.UpdateUserRequest
import com.useai.core.network.response.UserResponse
import javax.inject.Inject

class UsersRemoteDataSourceImpl @Inject constructor(
    private val usersApi: UsersApi
) : UsersRemoteDataSource {
    override suspend fun getUser(): UserResponse {
        return usersApi.getUser()
    }

    override suspend fun deleteUser() {
        return usersApi.deleteUser()
    }

    override suspend fun updateUser(request: UpdateUserRequest): UserResponse {
        return usersApi.updateUser(request)
    }

    override suspend fun getUserById(userId: String): UserResponse {
        return usersApi.getUserById(userId)
    }
}

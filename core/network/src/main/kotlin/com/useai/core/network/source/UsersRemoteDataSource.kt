package com.useai.core.network.source

import com.useai.core.network.request.UpdateUserRequest
import com.useai.core.network.response.UserResponse

interface UsersRemoteDataSource {
    suspend fun getUser(): UserResponse

    suspend fun deleteUser()

    suspend fun updateUser(request: UpdateUserRequest): UserResponse

    suspend fun getUserById(userId: String): UserResponse
}

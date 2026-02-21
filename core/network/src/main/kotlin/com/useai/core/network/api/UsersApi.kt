package com.useai.core.network.api

import com.useai.core.network.request.UpdateUserRequest
import com.useai.core.network.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UsersApi {
    @GET("/api/v1/users/me/")
    suspend fun getUser(): UserResponse

    @DELETE("/api/v1/users/me/")
    suspend fun deleteUser()

    @PATCH("/api/v1/users/me/")
    suspend fun updateUser(
        @Body request: UpdateUserRequest
    ): UserResponse

    @GET("/api/v1/users/")
    suspend fun getUserById(
        @Path("user_id") userId: String
    ): UserResponse
}

package com.useai.core.network.api

import GoogleLoginResponse
import com.useai.core.network.request.GoogleLoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/google/mobile/")
    suspend fun requestGoogleLogin(
        @Body request: GoogleLoginRequest
    ): GoogleLoginResponse

    @POST("/api/v1/auth/logout/")
    suspend fun requestGoogleLogout()
}

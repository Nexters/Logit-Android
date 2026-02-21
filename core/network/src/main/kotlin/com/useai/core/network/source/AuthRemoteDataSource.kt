package com.useai.core.network.source

import GoogleLoginResponse
import com.useai.core.network.request.GoogleLoginRequest

interface AuthRemoteDataSource {
    suspend fun requestGoogleLogin(request: GoogleLoginRequest): GoogleLoginResponse

    suspend fun requestGoogleLogout()
}

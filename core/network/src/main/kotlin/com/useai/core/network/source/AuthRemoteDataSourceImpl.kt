package com.useai.core.network.source

import GoogleLoginResponse
import com.useai.core.network.api.AuthApi
import com.useai.core.network.request.GoogleLoginRequest
import javax.inject.Inject

internal class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApi: AuthApi,
) : AuthRemoteDataSource {
    override suspend fun requestGoogleLogin(request: GoogleLoginRequest): GoogleLoginResponse {
        return authApi.requestGoogleLogin(request)
    }
}

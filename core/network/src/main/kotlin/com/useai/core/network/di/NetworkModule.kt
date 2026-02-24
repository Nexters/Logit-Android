package com.useai.core.network.di

import android.util.Log
import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.useai.core.common.qualifiers.AuthClient
import com.useai.core.common.qualifiers.BaseClient
import com.useai.core.common.qualifiers.RefreshClient
import com.useai.core.datastore.LogitPreferencesDataSource
import com.useai.core.network.BuildConfig
import com.useai.core.network.ChattingEventSourceFactory
import com.useai.core.network.api.AuthApi
import com.useai.core.network.error.RemoteErrorCallAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.URI

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object NetworkModule {

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    @Provides
    @ActivityRetainedScoped
    fun providesChattingEventSourceFactory(
        @AuthClient client: OkHttpClient
    ): ChattingEventSourceFactory {
        return ChattingEventSourceFactory { handler, request ->
            val requestJsonString = Json.encodeToString(request)
            BackgroundEventSource.Builder(
                handler,
                EventSource.Builder(
                    ConnectStrategy
                        .http(URI.create(BuildConfig.BASE_URL + "api/v1/projects/chats"))
                        .methodAndBody("POST", requestJsonString.toRequestBody(mediaType))
                        .httpClient(client)
                )
            ).build()
        }
    }

    @Provides
    @ActivityRetainedScoped
    fun providesJson() = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @ActivityRetainedScoped
    @BaseClient
    fun providesBaseRetrofit(
        @BaseClient client: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RemoteErrorCallAdapterFactory(json))
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @AuthClient
    fun providesAuthRetrofit(
        @AuthClient client: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RemoteErrorCallAdapterFactory(json))
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @RefreshClient
    fun providesRefreshRetrofit(
        @RefreshClient client: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RemoteErrorCallAdapterFactory(json))
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @BaseClient
    fun providesBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG)
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            }
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @AuthClient
    fun providesAuthOkHttpClient(
        @BaseClient baseClient: OkHttpClient,
        authInterceptor: Interceptor,
        authenticator: Authenticator
    ): OkHttpClient {
        return baseClient.newBuilder()
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @RefreshClient
    fun providesRefreshOkHttpClient(
        @BaseClient baseClient: OkHttpClient
    ): OkHttpClient {
        return baseClient.newBuilder().build()
    }

    @Provides
    @ActivityRetainedScoped
    fun providesAuthenticator(
        @RefreshClient lazyApi: Lazy<AuthApi>,
        logitPreferencesDataSource: LogitPreferencesDataSource
    ): Authenticator = Authenticator { _, response ->
        // 1. 현재 저장된 Access Token을 가져옴
        val currentAccessToken = runBlocking { logitPreferencesDataSource.accessToken.firstOrNull() }

        // 2. 요청에 사용된 토큰이 현재 저장된 토큰과 다르면, 이미 다른 곳에서 갱신된 것임
        if (response.request.header("Authorization")?.substringAfter("Bearer ") != currentAccessToken) {
            return@Authenticator response.request.newBuilder()
                .header("Authorization", "Bearer $currentAccessToken")
                .build()
        }

        synchronized(this) {
            // 3. 다시 한 번 토큰을 확인 (임계 영역 내에서 최신 상태 보장)
            val updatedAccessToken = runBlocking { logitPreferencesDataSource.accessToken.firstOrNull() }
            if (currentAccessToken != updatedAccessToken) {
                return@synchronized response.request.newBuilder()
                    .header("Authorization", "Bearer $updatedAccessToken")
                    .build()
            }

            // 4. Refresh Token 가져오기
            val refreshToken = runBlocking { logitPreferencesDataSource.refreshToken.firstOrNull() }
            if (refreshToken == null) {
                runBlocking { logitPreferencesDataSource.clear() }
                return@synchronized null
            }

            // 5. 토큰 갱신 요청
            val newAccessToken = runBlocking {
                try {
                    Log.d("Auth", "Attempting to refresh token...")
                    lazyApi.get().refreshAccessToken(
                        "Bearer $refreshToken",
                    )
                } catch (e: Exception) {
                    Log.e("Auth", "Refresh token failed: ${e.message}", e)
                    null
                }
            }

            // 6. 결과 처리
            if (newAccessToken == null) {
                runBlocking { logitPreferencesDataSource.clear() }
                return@synchronized null
            }

            Log.d("Auth", "Token refreshed successfully")
            runBlocking { logitPreferencesDataSource.setAccessToken(newAccessToken) }

            // 7. 새 토큰으로 기존 요청 재시도
            return@synchronized response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        }
    }

    @Provides
    @ActivityRetainedScoped
    fun providesAuthInterceptor(
        logitPreferencesDataSource: LogitPreferencesDataSource,
    ): Interceptor = Interceptor { chain: Interceptor.Chain ->
        val accessToken = runBlocking {
            logitPreferencesDataSource.accessToken.firstOrNull()
        }
        val request = chain.request()

        // 이미 Authorization 헤더가 있거나 토큰이 없는 경우 그대로 진행
        if (request.header("Authorization") != null || accessToken == null) {
            return@Interceptor chain.proceed(request)
        }

        val newRequest: Request = request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
        chain.proceed(newRequest)
    }
}

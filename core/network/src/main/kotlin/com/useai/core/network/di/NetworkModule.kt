package com.useai.core.network.di

import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.useai.core.common.qualifiers.AuthClient
import com.useai.core.common.qualifiers.BaseClient
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
    fun providesAuthenticator(
        lazyApi: Lazy<AuthApi>,
        logitPreferencesDataSource: LogitPreferencesDataSource
    ): Authenticator = Authenticator { _, response ->
        val currentToken = runBlocking { logitPreferencesDataSource.accessToken.firstOrNull() }

        if (response.request.header("Authorization")?.substringAfter("Bearer ") != currentToken) {
            return@Authenticator response.request.newBuilder()
                .header("Authorization", "Bearer $currentToken")
                .build()
        }

        synchronized(this) {
            val newCurrentToken = runBlocking { logitPreferencesDataSource.accessToken.firstOrNull() }
            if (currentToken != newCurrentToken) {
                return@synchronized response.request.newBuilder()
                    .header("Authorization", "Bearer $newCurrentToken")
                    .build()
            }

            val newAccessToken = runBlocking {
                try {
                    lazyApi.get().refreshAccessToken()
                } catch (_: Exception) {
                    null
                }
            }

            if (newAccessToken == null) {
                runBlocking { logitPreferencesDataSource.clear() }
                return@synchronized null
            }

            runBlocking { logitPreferencesDataSource.setAccessToken(newAccessToken) }

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
            BuildConfig.TEST_USER_ACCESS_TOKEN
        }
        val newRequest: Request = chain.request().newBuilder()
            .apply {
                if (accessToken != null) {
                    addHeader("Authorization", "Bearer $accessToken")
                }
            }
            .build()
        chain.proceed(newRequest)
    }
}

package com.useai.core.network.di

import com.launchdarkly.eventsource.ConnectStrategy
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.background.BackgroundEventSource
import com.useai.core.common.qualifiers.AuthClient
import com.useai.core.common.qualifiers.BaseClient
import com.useai.core.network.BuildConfig
import com.useai.core.network.ChattingEventSourceFactory
import com.useai.core.network.error.RemoteErrorCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.serialization.json.Json
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
    ) : ChattingEventSourceFactory {
        return ChattingEventSourceFactory { handler, request ->
            val requestJsonString = Json.encodeToString(request)
            BackgroundEventSource.Builder(
                handler,
                EventSource.Builder(
                    ConnectStrategy
                        .http(URI.create(BuildConfig.BASE_URL))
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
    ) : Retrofit {
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
    ) : Retrofit {
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
    fun providesBaseOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG)
                    addInterceptor(HttpLoggingInterceptor())
            }
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @AuthClient
    fun providesAuthOkHttpClient(
        @BaseClient baseClient: OkHttpClient,
        authInterceptor: Interceptor
    ) : OkHttpClient {
        // TODO : Authenticator
        return baseClient.newBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    fun providesAuthInterceptor(): Interceptor = Interceptor { chain: Interceptor.Chain ->
        val accessToken = BuildConfig.TEST_USER_ACCESS_TOKEN
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        chain.proceed(newRequest)
    }
}

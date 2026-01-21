package com.useai.core.network.di

import com.useai.core.common.qualifiers.AuthClient
import com.useai.core.common.qualifiers.BaseClient
import com.useai.core.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
internal object NetworkModule {

    @Provides
    @ActivityRetainedScoped
    @BaseClient
    fun providesBaseRetrofit(@BaseClient client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @ActivityRetainedScoped
    @AuthClient
    fun providesAuthRetrofit(@AuthClient client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
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

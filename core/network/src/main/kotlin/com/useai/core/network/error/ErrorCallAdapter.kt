package com.useai.core.network.error

import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class RemoteErrorCallAdapterFactory(
    private val json: Json = Json
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<Any, Call<Any>>? {
        if (getRawType(returnType) != Call::class.java) return null

        val responseType = (returnType as ParameterizedType).actualTypeArguments[0]
        return RemoteErrorCallAdapter(responseType, json)
    }

    private class RemoteErrorCallAdapter<R>(
        private val responseType: Type, private val json: Json
    ) : CallAdapter<R, Call<R>> {

        override fun responseType(): Type = responseType

        override fun adapt(call: Call<R>): Call<R> {
            return object : Call<R> {
                override fun enqueue(callback: Callback<R>) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (response.isSuccessful) {
                                if (response.body() != null)
                                    callback.onResponse(call, response)
                                else {
                                    callback.onFailure(
                                        call, RemoteError(
                                            response = response,
                                            errorCode = 0,
                                            message = "Empty body",
                                        )
                                    )
                                }
                            } else {
                                val errJson = response.errorBody()?.string()
                                val errResp = try {
                                    errJson?.let { json.decodeFromString<NetworkErrorResponse>(it) }
                                } catch (_: Exception) {
                                    null
                                }
                                callback.onFailure(
                                    call, RemoteError(
                                        response = response,
                                        errorCode = errResp?.code ?: 0,
                                        message = errResp?.message ?: response.message(),
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            callback.onFailure(call, t)
                        }
                    })
                }

                override fun execute(): Response<R> {
                    val response = call.execute()
                    if (response.isSuccessful) return response
                    val errJson = response.errorBody()?.string()
                    val errResp = try {
                        errJson?.let { json.decodeFromString<NetworkErrorResponse>(it) }
                    } catch (_: Exception) {
                        null
                    }
                    throw RemoteError(
                        response = response,
                        errorCode = errResp?.code ?: 0,
                        message = errResp?.message ?: response.message(),
                    )
                }

                override fun clone(): Call<R> = adapt(call.clone())
                override fun isExecuted(): Boolean = call.isExecuted
                override fun cancel() = call.cancel()
                override fun isCanceled(): Boolean = call.isCanceled
                override fun request(): Request = call.request()
                override fun timeout(): Timeout = call.timeout()
            }
        }
    }
}

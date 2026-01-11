package com.useai.core.network.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: 응답 명세 확정 후 수정
@Serializable
internal data class NetworkErrorResponse(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String
)
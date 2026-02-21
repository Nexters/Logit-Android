package com.useai.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val email: String? = null,
    @SerialName("full_name") val fullName: String? = null,
)

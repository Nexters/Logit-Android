package com.useai.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenBalanceResponse(
    @SerialName("balance") val balance: Int,
)

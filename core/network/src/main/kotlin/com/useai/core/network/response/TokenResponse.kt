package com.useai.core.network.response

import com.useai.core.model.account.TokenGrant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("signup_bonus_amount") val signupBonusAmount: Int = 0,
    @SerialName("monthly_grant_amount") val monthlyGrantAmount: Int = 0,
    @SerialName("attendance_amount") val attendanceAmount: Int = 0,
)

fun TokenResponse.toTokenGrant(): TokenGrant = TokenGrant(
    signupBonusAmount = signupBonusAmount,
    monthlyGrantAmount = monthlyGrantAmount,
    attendanceAmount = attendanceAmount,
)

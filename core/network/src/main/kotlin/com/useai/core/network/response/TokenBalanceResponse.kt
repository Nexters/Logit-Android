package com.useai.core.network.response

import com.useai.core.model.account.TokenBalance
import com.useai.core.model.account.TokenGrant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenBalanceResponse(
    @SerialName("balance") val balance: Int,
    @SerialName("monthly_tokens") val monthlyTokens: Int = 0,
    @SerialName("signup_bonus_received") val signupBonusReceived: Boolean = false,
    @SerialName("signup_bonus_amount") val signupBonusAmount: Int = 0,
    @SerialName("monthly_grant_received") val monthlyGrantReceived: Boolean = false,
    @SerialName("monthly_grant_amount") val monthlyGrantAmount: Int = 0,
    @SerialName("attendance_received") val attendanceReceived: Boolean = false,
    @SerialName("attendance_amount") val attendanceAmount: Int = 0,
    @SerialName("referral_reward_received") val referralRewardReceived: Boolean = false,
    @SerialName("referral_reward_amount") val referralRewardAmount: Int = 0,
    @SerialName("referral_reward_count") val referralRewardCount: Int = 0,
)

fun TokenBalanceResponse.toTokenBalance() = TokenBalance(
    balance = balance,
    totalAmount = monthlyTokens,
)

fun TokenBalanceResponse.toTokenGrant() = TokenGrant(
    signupBonusAmount = signupBonusAmount.takeIf { signupBonusReceived } ?: 0,
    monthlyGrantAmount = monthlyGrantAmount.takeIf { monthlyGrantReceived } ?: 0,
    attendanceAmount = attendanceAmount.takeIf { attendanceReceived } ?: 0,
    referralRewardAmount = referralRewardAmount.takeIf { referralRewardReceived } ?: 0,
    referralRewardCount = referralRewardCount.takeIf { referralRewardReceived } ?: 0,
)

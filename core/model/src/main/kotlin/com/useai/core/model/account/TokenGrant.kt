package com.useai.core.model.account

data class TokenGrant(
    val signupBonusAmount: Int = 0,
    val monthlyGrantAmount: Int = 0,
    val attendanceAmount: Int = 0,
    val referralRewardAmount: Int = 0,
    val referralRewardCount: Int = 0,
)

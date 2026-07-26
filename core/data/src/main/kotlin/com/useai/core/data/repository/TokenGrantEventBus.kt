package com.useai.core.data.repository

import com.useai.core.model.account.TokenGrant
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@ActivityRetainedScoped
class TokenGrantEventBus @Inject constructor() {
    private val mutableTokenGrants = MutableSharedFlow<TokenGrant>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    val tokenGrants = mutableTokenGrants.asSharedFlow()

    suspend fun emit(tokenGrant: TokenGrant) {
        if (tokenGrant.hasGrant()) {
            mutableTokenGrants.emit(tokenGrant)
        }
    }

    private fun TokenGrant.hasGrant(): Boolean {
        return signupBonusAmount > 0 ||
            monthlyGrantAmount > 0 ||
            attendanceAmount > 0 ||
            referralRewardAmount > 0
    }
}

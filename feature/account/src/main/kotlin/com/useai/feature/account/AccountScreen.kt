package com.useai.feature.account

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.AccountRepository
import com.useai.core.data.repository.TokenRepository
import com.useai.core.model.account.TokenBalance
import com.useai.core.model.account.UserProfile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.transform
import kotlinx.parcelize.Parcelize

@Parcelize
data object AccountScreen : Screen {
    data class State(
        val userProfile: UserProfile,
        val reportNotificationEnabled: Boolean,
        val showLogoutDialog: Boolean,
        val showWithdrawDialog: Boolean,
        val showGuideWebView: Boolean,
        val showProfileWebView: Boolean,
        val tokenBalance: Int,
        val tokenLimit: Int,
        val isTokenBalanceLoaded: Boolean,
        val effects: Flow<Effect> = emptyFlow(),
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Effect {
        data class TokenGranted(
            val amount: Int,
        ) : Effect
    }

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data object ReportNotificationSettingUpdated : Event
        data object Contact : Event
        data object AccountClicked : Event
        data object GuideClicked : Event
        data object DismissProfileWebView : Event
        data object DismissGuideWebView : Event
        data object LogoutClicked : Event
        data object DismissLogoutDialog : Event
        data object ConfirmLogout : Event
        data object WithdrawClicked : Event
        data object DismissWithdrawDialog : Event
        data object ConfirmWithdraw : Event
    }
}

class AccountPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val accountRepository: AccountRepository,
    private val tokenRepository: TokenRepository,
) : Presenter<AccountScreen.State> {
    @Composable
    override fun present(): AccountScreen.State {
        val effects = rememberRetained {
            tokenRepository.tokenGrants.transform { tokenGrant ->
                val totalGrantedAmount = tokenGrant.signupBonusAmount +
                    tokenGrant.monthlyGrantAmount +
                    tokenGrant.attendanceAmount +
                    tokenGrant.referralRewardAmount

                if (totalGrantedAmount > 0) {
                    emit(
                        AccountScreen.Effect.TokenGranted(
                            amount = totalGrantedAmount,
                        )
                    )
                }
            }
        }
        val userProfile by produceRetainedState(initialValue = UserProfile("", "")) {
            accountRepository.getUser()
                .onSuccess {
                    value = UserProfile(it.fullName, it.profileImageUrl)
                }
                .onFailure {
                    Log.e(TAG, "getUser failed: $it")
                }
        }
        val tokenBalance by produceRetainedState<TokenBalance?>(initialValue = null) {
            tokenRepository.getTokenBalance()
                .onSuccess {
                    value = it
                }
                .onFailure {
                    Log.e(TAG, "getTokenBalance failed: $it")
                }
        }
        var reportNotificationEnabled by rememberRetained { mutableStateOf(false) }
        var showLogoutDialog by rememberRetained { mutableStateOf(false) }
        var showWithdrawDialog by rememberRetained { mutableStateOf(false) }
        var showGuideWebView by rememberRetained { mutableStateOf(false) }
        var showProfileWebView by rememberRetained { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        return AccountScreen.State(
            userProfile = userProfile,
            reportNotificationEnabled = reportNotificationEnabled,
            showLogoutDialog = showLogoutDialog,
            showWithdrawDialog = showWithdrawDialog,
            showGuideWebView = showGuideWebView,
            showProfileWebView = showProfileWebView,
            tokenBalance = tokenBalance?.balance ?: 0,
            tokenLimit = tokenBalance?.totalAmount ?: 0,
            isTokenBalanceLoaded = tokenBalance != null,
            effects = effects,
        ) { event ->
            when (event) {
                AccountScreen.Event.Back -> navigator.pop()

                is AccountScreen.Event.ReportNotificationSettingUpdated -> {
                    val enabled = reportNotificationEnabled.not()
                    Log.d(TAG, "ReportNotificationSettingUpdated: $enabled")
                    reportNotificationEnabled = enabled

                    if (enabled) {
                        // TODO: 커리어 리포트 업데이트 알림 표시 On
                    } else {
                        // TODO: 커리어 리포트 업데이트 알림 표시 Off
                    }
                }

                AccountScreen.Event.Contact -> {
                    // TODO: 문의하기
                }

                AccountScreen.Event.AccountClicked -> {
                    showProfileWebView = true
                }

                AccountScreen.Event.GuideClicked -> {
                    showGuideWebView = true
                }

                AccountScreen.Event.DismissProfileWebView -> {
                    showProfileWebView = false
                }

                AccountScreen.Event.DismissGuideWebView -> {
                    showGuideWebView = false
                }

                AccountScreen.Event.LogoutClicked -> {
                    showLogoutDialog = true
                }

                AccountScreen.Event.DismissLogoutDialog -> {
                    showLogoutDialog = false
                }

                AccountScreen.Event.ConfirmLogout -> {
                    showLogoutDialog = false
                    scope.launch {
                        accountRepository.requestLogout().onSuccess {
                            accountRepository.clear()
                        }.onFailure {
                            Log.e(TAG, "Logout failed: $it")
                        }
                        navigator.resetRoot(LoginScreen)
                    }
                }

                AccountScreen.Event.WithdrawClicked -> {
                    showWithdrawDialog = true
                }

                AccountScreen.Event.DismissWithdrawDialog -> {
                    showWithdrawDialog = false
                }

                AccountScreen.Event.ConfirmWithdraw -> {
                    showWithdrawDialog = false
                    scope.launch {
                        accountRepository.deleteUser()
                            .onSuccess {
                                accountRepository.clear()
                                navigator.resetRoot(LoginScreen)
                            }
                            .onFailure {
                                Log.e(TAG, "Withdraw failed: $it")
                            }
                    }
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(AccountScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): AccountPresenter
    }

    companion object {
        private val TAG = AccountPresenter::class.simpleName
    }
}

package com.useai.feature.account

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
class AccountScreen : Screen {
    data class State(
        val userName: String,
        val reportNotificationEnabled: Boolean,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data object ReportNotificationSettingUpdated : Event
        data object Contact : Event
        data object Logout : Event
        data object Withdraw : Event
    }
}

class AccountPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<AccountScreen.State> {
    @Composable
    override fun present(): AccountScreen.State {
        var reportNotificationEnabled by rememberRetained { mutableStateOf(false) }

        return AccountScreen.State(
            userName = "로짓", // TODO: Google 사용자 이름 가져오기
            reportNotificationEnabled = reportNotificationEnabled,
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

                AccountScreen.Event.Logout -> {
                    // TODO: 로그아웃
                }

                AccountScreen.Event.Withdraw -> {
                    // TODO: 회원탈퇴
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

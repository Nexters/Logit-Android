package com.useai.feature.account

import android.util.Log
import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
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
class LoginScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        object TermsClicked : Event
        object PrivacyClicked : Event
    }
}

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<LoginScreen.State> {
    @Composable
    override fun present(): LoginScreen.State {
        return LoginScreen.State { event ->
            when (event) {
                LoginScreen.Event.TermsClicked -> {
                    Log.d(TAG, "TermsClicked")
                    // TODO: 이용약관 화면으로 이동
                }
                LoginScreen.Event.PrivacyClicked -> {
                    Log.d(TAG, "PrivacyClicked")
                    // TODO: 개인정보 처리방침 화면으로 이동
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): LoginPresenter
    }

    companion object {
        private val TAG = LoginPresenter::class.simpleName
    }
}

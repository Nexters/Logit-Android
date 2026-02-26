package com.useai.logit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.AccountRepository
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.account.LoginScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

@Parcelize
data object SplashScreen : Screen {
    data object State : CircuitUiState
}

class SplashPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val accountRepository: AccountRepository,
) : Presenter<SplashScreen.State> {
    @Composable
    override fun present(): SplashScreen.State {
        LaunchedEffect(Unit) {
            val startTime = System.currentTimeMillis()
            val isLoggedIn = checkLoginStatus()

            val elapsedTime = System.currentTimeMillis() - startTime
            val remainingTime = 1000L - elapsedTime
            if (remainingTime > 0) {
                delay(remainingTime)
            }

            if (isLoggedIn) {
                navigator.resetRoot(RootScreen)
            } else {
                navigator.resetRoot(LoginScreen)
            }
        }
        return SplashScreen.State
    }

    private suspend fun checkLoginStatus(): Boolean {
        return accountRepository.isLoggedIn()
    }

    @AssistedFactory
    @CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(navigator: Navigator): SplashPresenter
    }
}

@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
@Composable
fun SplashUi(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo_splash),
            contentDescription = null,
            modifier = Modifier.size(width = 174.dp, height = 60.dp)
        )
    }
}

@Preview
@Composable
private fun SplashUiPreview() {
    LogitTheme {
        SplashUi()
    }
}

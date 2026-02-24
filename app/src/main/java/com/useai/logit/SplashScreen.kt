package com.useai.logit

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
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
) : Presenter<SplashScreen.State> {
    @Composable
    override fun present(): SplashScreen.State {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            val startTime = System.currentTimeMillis()
            val isLoggedIn = checkLoginStatus(context)
            
            val elapsedTime = System.currentTimeMillis() - startTime
            val remainingTime = 1000L - elapsedTime
            if (remainingTime > 0) {
                delay(remainingTime)
            }
navigator.resetRoot(RootScreen)
//            if (isLoggedIn) {
//                navigator.resetRoot(RootScreen)
//            } else {
//                navigator.resetRoot(LoginScreen)
//            }
        }
        return SplashScreen.State
    }

    private suspend fun checkLoginStatus(context: Context): Boolean {
        // TODO: DataStore에 자동 로그인 정보 읽어서 로그인 상태 확인 로직 구현
        return false
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
    state: SplashScreen.State,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(LogitTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo_splash),
            contentDescription = null,
            modifier = Modifier.size(width = 174.dp, height = 60.dp)
        )
    }
}

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
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
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
        LaunchedEffect(Unit) {
            delay(1000) // TODO: 로그인 및 프로젝트 목록 로딩 완료까지 대기
            navigator.resetRoot(RootScreen)
        }
        return SplashScreen.State
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

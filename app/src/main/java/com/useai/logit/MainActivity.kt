package com.useai.logit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.useai.core.data.repository.TokenRepository
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.component.snackbar.showLogitSnackbar
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.navigation.ScreenProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var tokenRepository: TokenRepository

    private val screenProvider: ScreenProvider = ScreenProviderImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogitTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val signupBonusMessage = stringResource(R.string.token_signup_bonus_banner)
                val monthlyGrantMessage = stringResource(R.string.token_monthly_grant_banner)
                val attendanceMessage = stringResource(R.string.token_attendance_banner)
                val referralRewardMessage =
                    stringResource(R.string.token_referral_reward_banner)

                LaunchedEffect(tokenRepository, snackbarHostState) {
                    tokenRepository.tokenGrants.collect { tokenGrant ->
                        listOf(
                            tokenGrant.signupBonusAmount to signupBonusMessage,
                            tokenGrant.monthlyGrantAmount to monthlyGrantMessage,
                            tokenGrant.attendanceAmount to attendanceMessage,
                            tokenGrant.referralRewardAmount to referralRewardMessage,
                        ).forEach { (amount, message) ->
                            if (amount > 0) {
                                snackbarHostState.showLogitSnackbar(
                                    message = message.format(amount),
                                    iconResId = R.drawable.ic_complete,
                                )
                            }
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalScreenProvider provides screenProvider,
                    LocalLogitSnackbarHostState provides snackbarHostState
                ) {
                    CircuitCompositionLocals(circuit) {
                        val backStack = rememberSaveableBackStack(root = SplashScreen)
                        val navigator = rememberCircuitNavigator(backStack)

                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
                            decoration = NavigatorDefaults.EmptyDecoration,
                        )
                    }
                }
            }
        }
    }
}

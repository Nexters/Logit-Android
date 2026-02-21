package com.useai.logit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.navigation.ScreenProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    private val screenProvider: ScreenProvider = ScreenProviderImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogitTheme {
                val snackbarHostState = remember { SnackbarHostState() }
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
                            // decoration = GestureNavigationDecoration(onBackInvoked = navigator::pop)
                        )
                    }
                }
            }
        }
    }
}

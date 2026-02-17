package com.useai.logit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
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
                CompositionLocalProvider(LocalScreenProvider provides screenProvider) {
                    CircuitCompositionLocals(circuit) {
                        val backStack = rememberSaveableBackStack(root = SplashScreen)
                        val navigator = rememberCircuitNavigator(backStack)

                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
                            // decoration = GestureNavigationDecoration(onBackInvoked = navigator::pop)
                        )
                    }
                }
            }
        }
    }
}

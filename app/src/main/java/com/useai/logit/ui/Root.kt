package com.useai.logit.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.useai.core.designsystem.component.LogitNavigationBar
import com.useai.core.designsystem.component.LogitNavigationBarItem
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.component.snackbar.LogitSnackbarHost
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.chat.ChatScreen
import com.useai.feature.experience.ExperienceScreen
import com.useai.feature.home.HomeScreen
import com.useai.feature.newproject.NewProjectBasicInfoScreen
import com.useai.feature.report.ReportScreen
import com.useai.logit.RootScreen
import com.useai.logit.navigation.TopLevelNavItem
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(RootScreen::class, ActivityRetainedComponent::class)
fun Root(
    rootUiState: RootScreen.RootUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = LocalLogitSnackbarHostState.current

    val screens = remember {
        listOf(
            HomeScreen,
            ChatScreen(""),
            NewProjectBasicInfoScreen,
            ExperienceScreen,
            ReportScreen,
        )
    }
    val shouldShowBottomBar by remember(rootUiState.displayedScreen) {
        derivedStateOf {
            rootUiState.displayedScreen != NewProjectBasicInfoScreen &&
                screens.any { it == rootUiState.displayedScreen }
        }
    }
    val rootContainerColor = if (rootUiState.displayedScreen == ExperienceScreen) {
        LogitTheme.colors.gray20
    } else {
        LogitTheme.colors.white
    }

    BackHandler(enabled = rootUiState.canPop) {
        rootUiState.eventSink(RootScreen.RootEvent.NestedNavEvent(NavEvent.Pop()))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = rootContainerColor,
        snackbarHost = {
            LogitSnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets.statusBars,
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                LogitNavigationBar {
                    screens.forEach { screen ->
                        val navItem = TopLevelNavItem.fromScreen(screen)
                        LogitNavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(navItem.unselectedIconId),
                                    contentDescription = stringResource(navItem.titleTextId),
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    painter = painterResource(navItem.selectedIconId),
                                    contentDescription = stringResource(navItem.titleTextId),
                                )
                            },
                            labelText = stringResource(navItem.titleTextId),
                            selected = screen == rootUiState.displayedScreen,
                            alwaysShowLabel = true,
                            onClick = {
                                rootUiState.eventSink(RootScreen.RootEvent.ChangeScreen(screen))
                            },
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        CircuitContent(
            screen = rootUiState.displayedScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onNavEvent = { navEvent ->
                rootUiState.eventSink(RootScreen.RootEvent.NestedNavEvent(navEvent))
            }
        )
    }
}

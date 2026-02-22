package com.useai.logit.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.runtime.ExperimentalCircuitApi
import com.useai.core.designsystem.component.LogitNavigationBar
import com.useai.core.designsystem.component.LogitNavigationBarItem
import com.useai.core.designsystem.component.snackbar.LocalLogitSnackbarHostState
import com.useai.core.designsystem.component.snackbar.LogitSnackbarHost
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.experience.ExperienceScreen
import com.useai.feature.home.HomeScreen
import com.useai.feature.newproject.NewProjectBasicInfoScreen
import com.useai.feature.newproject.NewProjectQuestionScreen
import com.useai.feature.projects.ProjectsScreen
import com.useai.feature.report.ReportScreen
import com.useai.logit.RootScreen
import com.useai.logit.navigation.TopLevelNavItem
import dagger.hilt.android.components.ActivityRetainedComponent

@OptIn(ExperimentalCircuitApi::class)
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
            ProjectsScreen,
            NewProjectBasicInfoScreen,
            ExperienceScreen,
            ReportScreen,
        )
    }
    val topScreen = rootUiState.backStack.topRecord?.screen
    val shouldShowBottomBar by remember(topScreen) {
        derivedStateOf {
            val isNewProjectFlow =
                topScreen is NewProjectBasicInfoScreen || topScreen is NewProjectQuestionScreen
            val isTabRoot = screens.any { it == topScreen }
            
            isTabRoot && !isNewProjectFlow
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            LogitSnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = EnterTransition.None,
                exit = ExitTransition.None
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
                            selected = screen == topScreen,
                            alwaysShowLabel = true,
                            onClick = {
                                rootUiState.eventSink(RootScreen.RootEvent.ChangeScreen(screen))
                            },
                        )
                    }
                }
            }
        },
        containerColor = LogitTheme.colors.white,
    ) { paddingValues ->
        NavigableCircuitContent(
            navigator = rootUiState.navigator,
            backStack = rootUiState.backStack,
            decoration = NavigatorDefaults.EmptyDecoration,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

package com.useai.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.account.UserProfile
import com.useai.core.model.project.ProjectListItem
import com.useai.core.ui.AppHeader
import com.useai.core.ui.ExperienceType
import com.useai.core.ui.LogitDialog
import com.useai.core.ui.LogitExperienceBanner
import com.useai.core.ui.LogitFormTitle
import com.useai.core.ui.project.EmptyProjectList
import com.useai.core.ui.project.projectList
import com.useai.feature.home.HomeScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
@CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
fun Home(
    modifier: Modifier = Modifier,
    state: HomeScreen.State,
) {
    if (state.showProjectDeleteDialog && state.openedProjectMenuId != null) {
        LogitDialog(
            title = stringResource(R.string.project_delete_dialog_title),
            description = stringResource(R.string.project_delete_dialog_desc),
            confirmText = stringResource(R.string.project_delete_dialog_confirm),
            onConfirm = { state.eventSink(HomeScreen.Event.DeleteProjectConfirmed(state.openedProjectMenuId)) },
            cancelText = stringResource(R.string.project_delete_dialog_cancel),
            onCancel = { state.eventSink(HomeScreen.Event.DeleteProjectCanceled) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        AppHeader(
            title = {
                Image(
                    painter = painterResource(R.drawable.ic_symbol_word),
                    contentDescription = stringResource(R.string.content_description_app_logo),
                    modifier = Modifier
                        .height(28.dp)
                        .width(85.dp),
                )
            },
            iconModel = state.userProfile.userImageUrl.ifEmpty { R.drawable.ic_app_user },
            placeholder = painterResource(R.drawable.ic_app_user),
            iconDescription = stringResource(R.string.content_description_user_profile),
            iconSize = dimensionResource(R.dimen.app_header_user_profile_image_size),
            onIconClick = {
                state.eventSink(HomeScreen.Event.AccountClicked)
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = state.scrollState,
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(Modifier.height(22.dp))
                    LogitFormTitle(
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                        ),
                        title = stringResource(R.string.home_experience_type_title_format),
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_form_vertical)))
                    LogitExperienceBanner(state.bannerItems)
                    Spacer(Modifier.height(43.dp))
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                        ),
                ) {
                    LogitFormTitle(
                        title = stringResource(R.string.home_project_list_title),
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            if (state.projects.isEmpty()) {
                item {
                    EmptyProjectList(
                        onClickCreateProject = {
                            state.eventSink(HomeScreen.Event.NewProjectClicked)
                        },
                    )
                }
            } else {
                projectList(
                    projects = state.projects,
                    onClickProject = { projectId ->
                        state.eventSink(HomeScreen.Event.ProjectClicked(projectId))
                    },
                    openedProjectMenuId = state.openedProjectMenuId,
                    isDeleting = state.isDeletingProject,
                    onClickProjectMore = { projectId ->
                        state.eventSink(HomeScreen.Event.ProjectMoreClicked(projectId))
                    },
                    onDismissProjectMenu = {
                        state.eventSink(HomeScreen.Event.DismissProjectMenu)
                    },
                    onClickDeleteProject = { projectId ->
                        state.eventSink(HomeScreen.Event.DeleteProjectClicked(projectId))
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeWithEmptyProjectPreview() {
    LogitTheme {
        Scaffold(
            containerColor = LogitTheme.colors.white,
        ) { paddingValues ->
            Home(
                modifier = Modifier.padding(paddingValues),
                state = HomeScreen.State(
                    userProfile = UserProfile("로짓", ""),
                    bannerItems = ExperienceType.entries,
                    projects = emptyList(),
                    openedProjectMenuId = null,
                    showProjectDeleteDialog = false,
                    isDeletingProject = false,
                    scrollState = rememberLazyListState(),
                ),
            )
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    LogitTheme {
        Scaffold(
            containerColor = LogitTheme.colors.white,
        ) { paddingValues ->
            Home(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = HomeScreen.State(
                    userProfile = UserProfile("로짓", ""),
                    bannerItems = ExperienceType.entries,
                    projects = listOf(
                        ProjectListItem(
                            id = "1",
                            company = "카카오페이",
                            jobPosition = "디자인 어시스턴트 어쩌구 저쩌구 어쩌구 저쩌구",
                            dueDate = LocalDate.of(2026, 1, 7),
                            questionId = "",
                            totalQuestions = 0,
                            completedQuestions = 0,
                            updatedAt = LocalDateTime.now()
                        ),
                        ProjectListItem(
                            id = "2",
                            company = "네이버",
                            jobPosition = "프론트엔드 개발",
                            dueDate = LocalDate.of(2026, 1, 7),
                            questionId = "",
                            totalQuestions = 0,
                            completedQuestions = 0,
                            updatedAt = LocalDateTime.now()
                        ),
                        ProjectListItem(
                            id = "3",
                            company = "토스",
                            jobPosition = "iOS 개발",
                            dueDate = LocalDate.of(2026, 1, 7),
                            questionId = "",
                            totalQuestions = 0,
                            completedQuestions = 0,
                            updatedAt = LocalDateTime.now()
                        ),
                    ),
                    openedProjectMenuId = null,
                    showProjectDeleteDialog = false,
                    isDeletingProject = false,
                    scrollState = rememberLazyListState(),
                ),
            )
        }
    }
}

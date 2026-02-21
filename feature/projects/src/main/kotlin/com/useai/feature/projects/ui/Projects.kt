package com.useai.feature.projects.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.useai.core.model.project.ProjectListItem
import com.useai.core.ui.AppHeader
import com.useai.core.ui.project.EmptyProjectList
import com.useai.core.ui.project.ProjectList
import com.useai.feature.projects.ProjectsScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
@CircuitInject(ProjectsScreen::class, ActivityRetainedComponent::class)
fun Projects(
    modifier: Modifier = Modifier,
    state: ProjectsScreen.State,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        contentPadding = PaddingValues(
            bottom = dimensionResource(R.dimen.screen_common_padding_bottom),
        )
    ) {
        item {
            AppHeader(
                title = {
                    Text(
                        text = stringResource(R.string.home_project_list_title),
                        style = LogitTheme.typography.body1,
                        color = LogitTheme.colors.black,
                    )
                },
                iconPainter = painterResource(R.drawable.ic_tab_add),
                iconDescription = stringResource(R.string.content_description_add_project),
                iconSize = 16.dp,
                onIconClick = {
                    state.eventSink(ProjectsScreen.Event.NewProjectClicked)
                },
                paddingValues = PaddingValues(
                    start = dimensionResource(R.dimen.screen_common_padding_horizontal),
                ),
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                        vertical = 6.dp
                    ),
            ) {
                Text(
                    text = stringResource(R.string.projects_count_format, state.projects.size),
                    style = LogitTheme.typography.body8_1,
                    color = LogitTheme.colors.gray200,
                )
            }
        }

        item {
            if (state.projects.isEmpty()) {
                EmptyProjectList(
                    onClickCreateProject = {
                        state.eventSink(ProjectsScreen.Event.NewProjectClicked)
                    }
                )
            } else {
                this@LazyColumn.ProjectList(
                    projects = state.projects,
                    onClickProject = {
                        state.eventSink(ProjectsScreen.Event.ProjectClicked(it))
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProjectsPreview() {
    LogitTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Projects(
                modifier = Modifier.padding(paddingValues),
                state = ProjectsScreen.State(
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
                ),
            )
        }
    }
}

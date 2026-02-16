package com.useai.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.project.ProjectListItem
import com.useai.core.ui.AppHeader
import com.useai.core.ui.ExperienceBannerItem
import com.useai.core.ui.ExperienceType
import com.useai.core.ui.LogitExperienceBanner
import com.useai.core.ui.LogitFormTitle
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
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            AppHeader(
                onClickProfile = {
                    // TODO: go to user profile screen
                }
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.spacing_form_horizontal),
                        end = dimensionResource(R.dimen.spacing_form_horizontal),
                        top = 22.dp,
                        bottom = 0.dp // 하단 패딩을 제거하여 하위 아이템과의 이중 여백 방지
                    ),
            ) {
                LogitFormTitle(
                    title = stringResource(R.string.home_experience_type_title_format, "로짓"),
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.spacing_form_vertical)))
                LogitExperienceBanner(state.bannerItems)
                Spacer(Modifier.height(43.dp))
                LogitFormTitle(
                    title = stringResource(R.string.home_project_list_title),
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        if (state.projects.isEmpty()) {
            item {
                EmptyProjectList(
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_form_horizontal)),
                    onClickCreateProject = {
                        state.eventSink(HomeScreen.Event.CreateProject)
                    }
                )
            }
        } else {
            itemsIndexed(
                items = state.projects,
                key = { _, project -> project.id }
            ) { index, project ->
                ProjectItem(
                    project = project,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_form_horizontal))
                )
                if (index < state.projects.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.spacing_form_horizontal)),
                        thickness = 1.dp,
                        color = LogitTheme.colors.gray70
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyProjectList(
    modifier: Modifier = Modifier,
    onClickCreateProject: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_empty_state),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.home_empty_project_phrase),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.gray100,
        )
        Spacer(Modifier.height(17.dp))
        LogitPrimaryButton(
            text = stringResource(R.string.home_new_project),
            onClick = {
                onClickCreateProject()
            },
            textStyle = LogitTheme.typography.body6_2,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        )
    }
}

@Composable
private fun ProjectItem(
    project: ProjectListItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    // TODO: go to chat screen
                }
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 24.dp)
                .background(LogitTheme.colors.primary70)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(
                R.string.home_project_list_item_title_format,
                project.company,
                project.jobPosition
            ),
            style = LogitTheme.typography.body6_2,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = project.dueDate.toString(),
            style = LogitTheme.typography.body7_4,
        )
    }
}

@Preview
@Composable
private fun HomeWithEmptyProjectPreview() {
    LogitTheme {
        Scaffold(
            modifier = Modifier.background(LogitTheme.colors.white),
        ) { paddingValues ->
            Home(
                modifier = Modifier.padding(paddingValues),
                state = HomeScreen.State(
                    bannerItems = listOf(
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Leadership,
                            experienceCount = 7,
                        ),
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Expertise,
                            experienceCount = 1,
                        ),
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Analysis,
                            experienceCount = 3,
                        ),
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Creativity,
                            experienceCount = 30,
                        ),
                    ),
                    projects = emptyList(),
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
            modifier = Modifier.background(LogitTheme.colors.white),
        ) { paddingValues ->
            Home(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = HomeScreen.State(
                    bannerItems = listOf(
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Leadership,
                            experienceCount = 7,
                        ),
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Expertise,
                            experienceCount = 1,
                        ),
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Analysis,
                            experienceCount = 3,
                        ),
                        ExperienceBannerItem(
                            experienceType = ExperienceType.Creativity,
                            experienceCount = 30,
                        ),
                    ),
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
                    )
                ),
            )
        }
    }
}

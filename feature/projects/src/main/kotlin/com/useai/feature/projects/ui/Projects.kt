package com.useai.feature.projects.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.project.ProjectListItem
import com.useai.core.ui.AppHeader
import com.useai.core.ui.project.EmptyProjectList
import com.useai.feature.projects.ProjectsScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
@CircuitInject(ProjectsScreen::class, ActivityRetainedComponent::class)
fun Projects(
    modifier: Modifier = Modifier,
    state: ProjectsScreen.State,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
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

        if (state.projects.isEmpty()) {
            EmptyProjectList(
                modifier = Modifier.weight(1f),
                onClickCreateProject = {
                    state.eventSink(ProjectsScreen.Event.NewProjectClicked)
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    bottom = dimensionResource(R.dimen.screen_common_padding_bottom),
                )
            ) {
                itemsIndexed(
                    items = state.projects,
                    key = { _, item -> item.id }
                ) { index, item ->
                    ProjectListItemRow(
                        item = item,
                        onClick = { state.eventSink(ProjectsScreen.Event.ProjectClicked(item.id)) },
                    )
                    if (index < state.projects.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal)
                            ),
                            thickness = 1.dp,
                            color = LogitTheme.colors.gray70,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectListItemRow(
    item: ProjectListItem,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 24.dp)
                .background(
                    color = LogitTheme.colors.primary70,
                    shape = RoundedCornerShape(9.dp)
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = stringResource(R.string.home_project_list_item_title_format, item.company, item.jobPosition),
            style = LogitTheme.typography.body6_2,
            color = LogitTheme.colors.black,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.width(10.dp))
        DueStatusChip(status = item.resolveDueStatus())
    }
}

@Composable
private fun DueStatusChip(status: ProjectDueStatus) {
    Box(
        modifier = Modifier
            .background(
                color = if (status.isClosed) LogitTheme.colors.gray20 else LogitTheme.colors.primary20,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.label,
            style = LogitTheme.typography.body5_2,
            color = if (status.isClosed) LogitTheme.colors.gray200 else LogitTheme.colors.primary200,
        )
    }
}

private fun ProjectListItem.resolveDueStatus(today: LocalDate = LocalDate.now()): ProjectDueStatus {
    if (dueDate.year >= ALWAYS_OPEN_YEAR_THRESHOLD) return ProjectDueStatus.AlwaysOpen
    if (dueDate.isBefore(today)) return ProjectDueStatus.Closed
    if (dueDate.isEqual(today)) return ProjectDueStatus.DDay
    return ProjectDueStatus.Dn(ChronoUnit.DAYS.between(today, dueDate))
}

private sealed interface ProjectDueStatus {
    val label: String
    val isClosed: Boolean

    data object DDay : ProjectDueStatus {
        override val label: String = "D-Day"
        override val isClosed: Boolean = false
    }

    data class Dn(val remainingDays: Long) : ProjectDueStatus {
        override val label: String = "D-$remainingDays"
        override val isClosed: Boolean = false
    }

    data object AlwaysOpen : ProjectDueStatus {
        override val label: String = "상시"
        override val isClosed: Boolean = false
    }

    data object Closed : ProjectDueStatus {
        override val label: String = "마감"
        override val isClosed: Boolean = true
    }
}

private const val ALWAYS_OPEN_YEAR_THRESHOLD = 9000

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
                            company = "KakaoPay",
                            jobPosition = "Designer Assistant",
                            dueDate = LocalDate.of(2026, 1, 7),
                            questionId = "",
                            totalQuestions = 0,
                            completedQuestions = 0,
                            updatedAt = LocalDateTime.now()
                        ),
                        ProjectListItem(
                            id = "2",
                            company = "Naver",
                            jobPosition = "Frontend Developer",
                            dueDate = LocalDate.of(2026, 1, 7),
                            questionId = "",
                            totalQuestions = 0,
                            completedQuestions = 0,
                            updatedAt = LocalDateTime.now()
                        ),
                        ProjectListItem(
                            id = "3",
                            company = "Toss",
                            jobPosition = "iOS Developer",
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

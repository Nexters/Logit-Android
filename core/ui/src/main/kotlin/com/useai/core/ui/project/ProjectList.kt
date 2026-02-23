package com.useai.core.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.project.ProjectListItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LazyListScope.projectList(
    projects: List<ProjectListItem>,
    onClickProject: (String) -> Unit,
) {
    itemsIndexed(
        items = projects,
        key = { _, project -> project.id }
    ) { index, project ->
        ProjectItem(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal)),
            project = project,
            onClick = {
                onClickProject(project.id)
            },
        )
        if (index < projects.lastIndex) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal)),
                thickness = 1.dp,
                color = LogitTheme.colors.gray70
            )
        }
    }
}

@Composable
private fun ProjectItem(
    modifier: Modifier = Modifier,
    project: ProjectListItem,
    onClick: () -> Unit,
) {
    val dueStatus = project.resolveDueStatus()
    val isCompleted = project.isCompleted()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 60.dp)
                .background(
                    color = LogitTheme.colors.primary70,
                    shape = RoundedCornerShape(9.dp)
                )
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DueStatusChip(status = dueStatus)
                if (dueStatus.showDueDate) {
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = project.dueDate.format(DUE_DATE_FORMATTER),
                        style = LogitTheme.typography.body5_5,
                        color = LogitTheme.colors.gray200,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(
                        R.string.home_project_list_item_title_format,
                        project.company,
                        project.jobPosition
                    ),
                    style = LogitTheme.typography.body6_2,
                    color = LogitTheme.colors.black,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(8.dp))
            }
        }

        ActionStatusIcon(isCompleted = isCompleted)
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_more_vertical),
            contentDescription = null,
            tint = LogitTheme.colors.gray400,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
private fun DueStatusChip(
    status: ProjectDueStatus,
) {
    Box(
        modifier = Modifier
            .widthIn(min = 70.dp)
            .background(
                color = if (status.isClosed) LogitTheme.colors.gray20 else LogitTheme.colors.primary20,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.label,
            style = LogitTheme.typography.body5_2,
            color = if (status.isClosed) LogitTheme.colors.gray200 else LogitTheme.colors.primary200,
        )
    }
}

@Composable
private fun ActionStatusIcon(
    isCompleted: Boolean,
) {
    Icon(
        imageVector = if (isCompleted) ImageVector.vectorResource(R.drawable.ic_check) else ImageVector.vectorResource(R.drawable.ic_pen),
        contentDescription = null,
        tint = if (isCompleted) CompletedIconTint else LogitTheme.colors.secondary100,
        modifier = Modifier.size(24.dp)
    )
}

private fun ProjectListItem.resolveDueStatus(today: LocalDate = LocalDate.now()): ProjectDueStatus {
    if (dueDate.year >= ALWAYS_OPEN_YEAR_THRESHOLD) {
        return ProjectDueStatus.AlwaysOpen
    }
    if (dueDate.isBefore(today)) {
        return ProjectDueStatus.Closed
    }
    if (dueDate.isEqual(today)) {
        return ProjectDueStatus.DDay
    }
    return ProjectDueStatus.Dn(
        remainingDays = ChronoUnit.DAYS.between(today, dueDate)
    )
}

private fun ProjectListItem.isCompleted(): Boolean = totalQuestions > 0 && completedQuestions >= totalQuestions

private sealed interface ProjectDueStatus {
    val label: String
    val isClosed: Boolean
    val showDueDate: Boolean

    data object DDay : ProjectDueStatus {
        override val label: String = "D-Day"
        override val isClosed: Boolean = false
        override val showDueDate: Boolean = true
    }

    data class Dn(
        val remainingDays: Long,
    ) : ProjectDueStatus {
        override val label: String = "D-$remainingDays"
        override val isClosed: Boolean = false
        override val showDueDate: Boolean = true
    }

    data object AlwaysOpen : ProjectDueStatus {
        override val label: String = "상시"
        override val isClosed: Boolean = false
        override val showDueDate: Boolean = false
    }

    data object Closed : ProjectDueStatus {
        override val label: String = "마감"
        override val isClosed: Boolean = true
        override val showDueDate: Boolean = true
    }
}

private val DUE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd")
private val CompletedIconTint = Color(0xFF20C05C)
private const val ALWAYS_OPEN_YEAR_THRESHOLD = 9000

@Preview
@Composable
private fun ProjectListPreview() {
    LogitTheme {
        LazyColumn(
            modifier = Modifier.background(LogitTheme.colors.white)
        ) {
            projectList(
                projects = listOf(
                    ProjectListItem(
                        id = "1",
                        company = "KakaoPay",
                        jobPosition = "Design Assistant",
                        dueDate = LocalDate.now(),
                        questionId = "",
                        totalQuestions = 5,
                        completedQuestions = 1,
                        updatedAt = LocalDateTime.now()
                    ),
                    ProjectListItem(
                        id = "2",
                        company = "Naver",
                        jobPosition = "Frontend Developer",
                        dueDate = LocalDate.now().plusDays(5),
                        questionId = "",
                        totalQuestions = 5,
                        completedQuestions = 5,
                        updatedAt = LocalDateTime.now()
                    ),
                    ProjectListItem(
                        id = "3",
                        company = "Toss",
                        jobPosition = "Backend Developer",
                        dueDate = LocalDate.MAX,
                        questionId = "",
                        totalQuestions = 0,
                        completedQuestions = 0,
                        updatedAt = LocalDateTime.now()
                    ),
                    ProjectListItem(
                        id = "4",
                        company = "Line",
                        jobPosition = "iOS Developer",
                        dueDate = LocalDate.now().minusDays(2),
                        questionId = "",
                        totalQuestions = 3,
                        completedQuestions = 3,
                        updatedAt = LocalDateTime.now()
                    ),
                ),
                onClickProject = {},
            )
        }
    }
}

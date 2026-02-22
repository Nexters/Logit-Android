package com.useai.core.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.project.ProjectListItem
import java.time.LocalDate
import java.time.LocalDateTime

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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
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
private fun ProjectListPreview() {
    LogitTheme {
        LazyColumn(
            modifier = Modifier.background(LogitTheme.colors.white)
        ) {
            projectList(
                projects = listOf(
                    ProjectListItem(
                        id = "1",
                        company = "카카오페이",
                        jobPosition = "디자인 어시스턴트",
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
                ),
                onClickProject = {},
            )
        }
    }
}

package com.useai.core.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.project.ProjectListItem
import java.time.LocalDate
import java.time.temporal.ChronoUnit

sealed interface ProjectDueStatus {
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
        override val label: String = "\uC0C1\uC2DC"
        override val isClosed: Boolean = false
        override val showDueDate: Boolean = false
    }

    data object Closed : ProjectDueStatus {
        override val label: String = "\uB9C8\uAC10"
        override val isClosed: Boolean = true
        override val showDueDate: Boolean = true
    }
}

fun ProjectListItem.resolveProjectDueStatus(today: LocalDate = LocalDate.now()): ProjectDueStatus {
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

@Composable
fun ProjectDueStatusChip(
    status: ProjectDueStatus,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
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

private const val ALWAYS_OPEN_YEAR_THRESHOLD = 9000

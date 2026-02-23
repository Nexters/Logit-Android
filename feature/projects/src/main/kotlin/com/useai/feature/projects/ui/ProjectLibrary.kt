package com.useai.feature.projects.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.projects.ProjectLibraryScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.launch

@Composable
@CircuitInject(ProjectLibraryScreen::class, ActivityRetainedComponent::class)
fun ProjectLibrary(
    state: ProjectLibraryScreen.State,
    modifier: Modifier = Modifier,
) {
    val onBack = {
        when (state) {
            is ProjectLibraryScreen.State.Loading -> state.eventSink(ProjectLibraryScreen.Event.Back)
            is ProjectLibraryScreen.State.LoadFailed -> state.eventSink(ProjectLibraryScreen.Event.Back)
            is ProjectLibraryScreen.State.Success -> state.eventSink(ProjectLibraryScreen.Event.Back)
        }
    }

    BackHandler {
        onBack()
    }

    when (state) {
        is ProjectLibraryScreen.State.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(R.string.experience_loading))
            }
        }

        is ProjectLibraryScreen.State.LoadFailed -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(20.dp),
            ) {
                IconButton(onClick = { state.eventSink(ProjectLibraryScreen.Event.Back) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_navigate_left),
                        contentDescription = stringResource(R.string.content_description_navigate_back),
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.report_load_failed),
                    style = LogitTheme.typography.body6_2,
                    color = LogitTheme.colors.black,
                )
            }
        }

        is ProjectLibraryScreen.State.Success -> {
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()
            val selectedIndex = state.questions.indexOfFirst { it.id == state.selectedQuestionId }
                .takeIf { it >= 0 } ?: 0

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(LogitTheme.colors.white),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(start = 6.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { state.eventSink(ProjectLibraryScreen.Event.Back) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigate_left),
                            contentDescription = stringResource(R.string.content_description_navigate_back),
                            tint = LogitTheme.colors.black,
                        )
                    }
                    Text(
                        text = state.projectTitle,
                        style = LogitTheme.typography.body4,
                        color = LogitTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    Box {
                        IconButton(onClick = { state.eventSink(ProjectLibraryScreen.Event.ToggleMenu) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_more_vertical),
                                contentDescription = null,
                                tint = LogitTheme.colors.black,
                            )
                        }
                        DropdownMenu(
                            expanded = state.isMenuExpanded,
                            onDismissRequest = { state.eventSink(ProjectLibraryScreen.Event.DismissMenu) },
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.chat_edit)) },
                                onClick = { state.eventSink(ProjectLibraryScreen.Event.EditProject) },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_write),
                                        contentDescription = null,
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.chat_delete)) },
                                onClick = { state.eventSink(ProjectLibraryScreen.Event.DeleteProject) },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_trash),
                                        contentDescription = null,
                                    )
                                }
                            )
                        }
                    }
                }

                QuestionTabs(
                    state = state,
                    selectedIndex = selectedIndex,
                    onTabClick = { index ->
                        scope.launch {
                            listState.animateScrollToItem(index)
                        }
                        state.eventSink(ProjectLibraryScreen.Event.SelectQuestion(state.questions[index].id))
                    }
                )

                LibraryContents(
                    questions = state.questions,
                    listState = listState,
                )
            }
        }
    }
}

@Composable
private fun QuestionTabs(
    state: ProjectLibraryScreen.State.Success,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(state.questions.indices.toList()) { index ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .height(34.dp)
                    .background(
                        color = if (selected) LogitTheme.colors.primary20 else LogitTheme.colors.white,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = if (selected) LogitTheme.colors.primary100 else LogitTheme.colors.gray100
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTabClick(index) }
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Q${index + 1}",
                    style = if (selected) LogitTheme.typography.body5_2 else LogitTheme.typography.body5_5,
                    color = if (selected) LogitTheme.colors.primary100 else LogitTheme.colors.gray300,
                )
            }
        }
    }
}

@Composable
private fun LibraryContents(
    questions: List<com.useai.core.model.chat.Question>,
    listState: LazyListState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        itemsIndexed(questions, key = { _, q -> q.id }) { _, question ->
            Column {
                Text(
                    text = question.title,
                    style = LogitTheme.typography.body5_2,
                    color = LogitTheme.colors.gray400,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = question.letter.ifBlank { "-" },
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray400,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 30.dp),
                thickness = 1.dp,
                color = LogitTheme.colors.gray70,
            )
        }
    }
}

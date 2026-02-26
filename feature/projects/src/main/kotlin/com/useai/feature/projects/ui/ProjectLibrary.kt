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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.Question
import com.useai.core.ui.LogitDialog
import com.useai.core.ui.LogitDropdownMenu
import com.useai.core.ui.LogitDropdownMenuItem
import com.useai.core.ui.LogitPageLoadingView
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
            LogitPageLoadingView(
                modifier = modifier.fillMaxSize()
            )
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
            if (state.showDeleteDialog) {
                LogitDialog(
                    onDismissRequest = { state.eventSink(ProjectLibraryScreen.Event.DismissDeleteDialog) },
                    title = stringResource(R.string.project_library_delete_question_dialog_text),
                    description = stringResource(R.string.project_library_delete_question_dialog_description),
                    confirmText = stringResource(R.string.project_library_delete_question_dialog_confirm),
                    onConfirm = { state.eventSink(ProjectLibraryScreen.Event.ConfirmDeleteQuestion) },
                    cancelText = stringResource(R.string.project_library_delete_question_dialog_cancel),
                    onCancel = { state.eventSink(ProjectLibraryScreen.Event.DismissDeleteDialog) }
                )
            }

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
                        LogitDropdownMenu(
                            expanded = state.isMenuExpanded,
                            onDismissRequest = { state.eventSink(ProjectLibraryScreen.Event.DismissMenu) },
                        ) {
                            LogitDropdownMenuItem(
                                text = stringResource(R.string.chat_edit),
                                onClick = { state.eventSink(ProjectLibraryScreen.Event.EditProject) },
                                icon = ImageVector.vectorResource(R.drawable.ic_write),
                            )
                            LogitDropdownMenuItem(
                                text = stringResource(R.string.chat_delete),
                                onClick = { state.eventSink(ProjectLibraryScreen.Event.TryDeleteQuestion) },
                                icon = ImageVector.vectorResource(R.drawable.ic_trash_drop),
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

                Spacer(Modifier.height(17.dp))

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
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 6.dp,
                bottom = 9.dp,
            ),
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
    questions: List<Question>,
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
        }
    }
}

@Preview
@Composable
private fun ProjectLibraryPreview() {
    LogitTheme {
        ProjectLibrary(
            state = ProjectLibraryScreen.State.Success(
                projectTitle = "네이버 프론트엔드 개발",
                questions = listOf(
                    Question(
                        id = "1",
                        title = "[필수] 본 직무에 지원하게 된 동기와 본인이 이 포지션에 가장 적합한 후보라고 생각하는 이유를 작성해 주세요.",
                        maxLength = 1000,
                        letter = "최근 서비스 이용자가 급증하며 예상치 못한 기술적 난관에 봉착했습니다. 트래픽 증가로 인해 서버 유지비가 전월 대비 40%나 급상승했으며, 동시에 메인 페이지의 응답 속도가 현저히 느려져 사용자 이탈이 우려되는 상황이었습니다. 저는 '서버 비용 20% 절감'과 '로딩 속도 1초 이내 유지'라는 두 가지 상충하는 목표를 동시에 달성해야 하는 과제를 맡게 되었습니다.\n" +
                                "\n" +
                                "이러한 주도적인 최적화 노력을 통해 서버 비용을 기존 대비 25% 절감할 수 있었으며, 메인 페이지 로딩 속도를 0.8초까지 단축하며 사용자 경험을 크게 개선했습니다. 이러한 성과를 인정받아 팀 내 '이달의 개발자'로 선정되는 영광을 얻기도 했습니다.\n" +
                                "\n" +
                                "이 과정을 통해 기술적 고도화만큼이나 비즈니스적 효율성을 고려하는 개발자의 시야가 얼마나 중요한지 깨달았습니다. 데이터에 기반한 트러블슈팅이 단순한 코드 수정을 넘어 조직의 운영 가치를 극대화할 수 있음을 배웠으며, 입사 후에도 효율과 성능을 모두 잡는 엔지니어로 성장하겠습니다.",
                    ),
                    Question(
                        id = "2",
                        title = "[필수] 본 직무에 지원하게 된 동기와 본인이 이 포지션에 가장 적합한 후보라고 생각하는 이유를 작성해 주세요.",
                        maxLength = 1000,
                        letter = "최근 서비스 이용자가 급증하며 예상치 못한 기술적 난관에 봉착했습니다. 트래픽 증가로 인해 서버 유지비가 전월 대비 40%나 급상승했으며, 동시에 메인 페이지의 응답 속도가 현저히 느려져 사용자 이탈이 우려되는 상황이었습니다. 저는 '서버 비용 20% 절감'과 '로딩 속도 1초 이내 유지'라는 두 가지 상충하는 목표를 동시에 달성해야 하는 과제를 맡게 되었습니다.\n" +
                                "\n" +
                                "이러한 주도적인 최적화 노력을 통해 서버 비용을 기존 대비 25% 절감할 수 있었으며, 메인 페이지 로딩 속도를 0.8초까지 단축하며 사용자 경험을 크게 개선했습니다. 이러한 성과를 인정받아 팀 내 '이달의 개발자'로 선정되는 영광을 얻기도 했습니다.\n" +
                                "\n" +
                                "이 과정을 통해 기술적 고도화만큼이나 비즈니스적 효율성을 고려하는 개발자의 시야가 얼마나 중요한지 깨달았습니다. 데이터에 기반한 트러블슈팅이 단순한 코드 수정을 넘어 조직의 운영 가치를 극대화할 수 있음을 배웠으며, 입사 후에도 효율과 성능을 모두 잡는 엔지니어로 성장하겠습니다.",
                    ),
                ),
                selectedQuestionId = "0",
                isMenuExpanded = false,
                showDeleteDialog = false,
                eventSink = {},
            )
        )
    }
}

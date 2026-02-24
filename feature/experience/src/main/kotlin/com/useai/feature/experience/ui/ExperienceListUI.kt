package com.useai.feature.experience.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.tooling.preview.Preview
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceCategory
import com.useai.feature.experience.ExperienceScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate

@Composable
@CircuitInject(ExperienceScreen::class, ActivityRetainedComponent::class)
fun ExperienceListUI(
    state: ExperienceScreen.State,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is ExperienceScreen.State.Loading -> {
            ExperienceLoadingUI(
                modifier = modifier
                    .background(LogitTheme.colors.gray20)
                    .statusBarsPadding()
            )
        }

        is ExperienceScreen.State.LoadFailed -> {
            ExperienceLoadFailedUI(
                onRetry = {
                    state.eventSink(ExperienceScreen.Event.Retry)
                },
                modifier = modifier
                    .background(LogitTheme.colors.gray20)
                    .statusBarsPadding()
            )
        }

        is ExperienceScreen.State.Success -> {
            ExperienceSuccessUI(
                experiences = state.experiences,
                openedMenuExperienceId = state.openedMenuExperienceId,
                isDeleting = state.isDeleting,
                onClickAdd = {
                    state.eventSink(ExperienceScreen.Event.ClickAddExperience)
                },
                onClickRegister = {
                    state.eventSink(ExperienceScreen.Event.ClickRegisterExperience)
                },
                onClickExperienceCard = { experienceId ->
                    state.eventSink(ExperienceScreen.Event.ClickExperienceCard(experienceId))
                },
                onClickExperienceMore = { experienceId ->
                    state.eventSink(ExperienceScreen.Event.ClickExperienceMore(experienceId))
                },
                onDismissMenu = {
                    state.eventSink(ExperienceScreen.Event.DismissExperienceMenu)
                },
                onClickEditExperience = { experienceId ->
                    state.eventSink(ExperienceScreen.Event.ClickEditExperience(experienceId))
                },
                onClickDeleteExperience = { experienceId ->
                    state.eventSink(ExperienceScreen.Event.ClickDeleteExperience(experienceId))
                },
                modifier = modifier
                    .background(LogitTheme.colors.gray20)
                    .statusBarsPadding()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExperienceListUIPreview() {
    LogitTheme {
        ExperienceListUI(
            state = ExperienceScreen.State.Success(
                experiences = previewExperiences,
                openedMenuExperienceId = null,
                isDeleting = false,
                eventSink = {}
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExperienceEmptyUIPreview() {
    LogitTheme {
        ExperienceListUI(
            state = ExperienceScreen.State.Success(
                experiences = emptyList(),
                openedMenuExperienceId = null,
                isDeleting = false,
                eventSink = {}
            )
        )
    }
}

private val previewExperiences = List(4) { index ->
    Experience(
        id = index.toString(),
        tags = listOf("소통력", "협력적소통", "협력적소통"),
        situation = "",
        task = "",
        action = "",
        result = "",
        category = ExperienceCategory.COLLABORATIVE_COMMUNICATION,
        startDate = LocalDate.MIN,
        endDate = LocalDate.MIN,
        experienceType = "",
        formatType = "STAR",
        title = "넥스트즈 AI 자소서 프로젝트 경험 UI 구현"
    )
}

package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceCategory
import com.useai.core.ui.experience.ExperienceCard
import com.useai.feature.experience.ExperienceScreen
import dagger.hilt.android.components.ActivityRetainedComponent
import java.time.LocalDate
import com.useai.core.designsystem.R

@Composable
@CircuitInject(ExperienceScreen::class, ActivityRetainedComponent::class)
fun ExperienceList(
    state: ExperienceScreen.State,
    modifier: Modifier = Modifier
) {
    when (state) {
        is ExperienceScreen.State.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(LogitTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.experience_loading),
                    style = LogitTheme.typography.body6_1,
                    color = LogitTheme.colors.gray300
                )
            }
        }

        is ExperienceScreen.State.LoadFailed -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(LogitTheme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.experience_load_failed),
                        style = LogitTheme.typography.body6_1,
                        color = LogitTheme.colors.gray300,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LogitPrimaryButton(
                        text = stringResource(R.string.experience_retry),
                        onClick = {
                            state.eventSink(ExperienceScreen.Event.Retry)
                        },
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
        }

        is ExperienceScreen.State.Success -> {
            ExperienceListContent(
                experiences = state.experiences,
                onClickAdd = {
                    state.eventSink(ExperienceScreen.Event.ClickAddExperience)
                },
                onClickRegister = {
                    state.eventSink(ExperienceScreen.Event.ClickRegisterExperience)
                },
                onClickExperienceMore = { experienceId ->
                    state.eventSink(ExperienceScreen.Event.ClickExperienceMore(experienceId))
                },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ExperienceListContent(
    experiences: List<Experience>,
    onClickAdd: () -> Unit,
    onClickRegister: () -> Unit,
    onClickExperienceMore: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white)
            .padding(horizontal = 20.dp)
            .padding(top = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.experience_list_title),
                style = LogitTheme.typography.body1,
                color = LogitTheme.colors.black,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_tab_add),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onClickAdd)
            )
        }

        Text(
            text = stringResource(R.string.experience_count, experiences.size),
            style = LogitTheme.typography.body8_1,
            color = LogitTheme.colors.gray100,
            modifier = Modifier.padding(top = 10.dp)
        )

        if (experiences.isEmpty()) {
            EmptyExperienceContent(
                onClickRegister = onClickRegister,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
            ) {
                items(
                    items = experiences,
                    key = { experience -> experience.id }
                ) { experience ->
                    ExperienceCardItem(
                        experience = experience,
                        onClickMore = { onClickExperienceMore(experience.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ExperienceCardItem(
    experience: Experience,
    onClickMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = LogitTheme.colors.gray70,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(start = 18.dp, end = 12.dp, bottom = 14.dp),
    ) {
        ExperienceCard(
            experience = experience,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_more_vertical),
            tint = LogitTheme.colors.gray300,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onClickMore)
                .padding(4.dp)
        )
    }
}

@Composable
private fun EmptyExperienceContent(
    onClickRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logit_empty),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Text(
            text = stringResource(R.string.experience_empty),
            style = LogitTheme.typography.body5_5,
            color = LogitTheme.colors.gray100,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        LogitPrimaryButton(
            text = stringResource(R.string.experience_register),
            textStyle = LogitTheme.typography.body5_2,
            onClick = onClickRegister,
            modifier = Modifier.width(170.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExperienceListPreview() {
    LogitTheme {
        ExperienceList(
            state = ExperienceScreen.State.Success(
                experiences = previewExperiences,
                eventSink = {}
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExperienceEmptyPreview() {
    LogitTheme {
        ExperienceList(
            state = ExperienceScreen.State.Success(
                experiences = emptyList(),
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
        title = "넥스터즈 AI 자소서 프로젝트 경험 UI 구현"
    )
}

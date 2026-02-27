package com.useai.feature.experience.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.experience.Experience
import com.useai.core.model.experience.ExperienceCreateFormatType
import com.useai.core.model.experience.ExperienceType
import com.useai.core.ui.LogitDialog
import java.time.LocalDate

@Composable
internal fun ExperienceSuccessUI(
    experiences: List<Experience>,
    openedMenuExperienceId: String?,
    isDeleting: Boolean,
    onClickAdd: () -> Unit,
    onClickRegister: () -> Unit,
    onClickExperienceCard: (String) -> Unit,
    onClickExperienceMore: (String) -> Unit,
    onDismissMenu: () -> Unit,
    onClickEditExperience: (String) -> Unit,
    onClickDeleteExperience: (String) -> Unit,
    showDeleteDialog: Boolean,
    onDeleteConfirm: (String) -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDeleteDialog && openedMenuExperienceId != null) {
        LogitDialog(
            title = stringResource(R.string.experience_delete_dialog_title),
            description = stringResource(R.string.experience_delete_dialog_desc),
            confirmText = stringResource(R.string.experience_delete_dialog_confirm),
            onConfirm = {
                onDeleteConfirm(openedMenuExperienceId)
            },
            cancelText = stringResource(R.string.experience_delete_dialog_cancel),
            onCancel = {
                onDeleteCancel()
            },
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.gray20)
    ) {
        ExperienceListHeader(
            count = experiences.size,
            onClickAdd = onClickAdd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 18.dp)
        )

        if (experiences.isEmpty()) {
            ExperienceEmptyUI(
                onClickRegister = onClickRegister,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 110.dp, start = 20.dp, end = 20.dp)
            ) {
                items(
                    items = experiences,
                    key = { experience -> experience.id }
                ) { experience ->
                    ExperienceCardListItem(
                        experience = experience,
                        onClickCard = { onClickExperienceCard(experience.id) },
                        onClickMore = { onClickExperienceMore(experience.id) },
                        isMenuExpanded = openedMenuExperienceId == experience.id,
                        isDeleting = isDeleting,
                        onDismissMenu = onDismissMenu,
                        onClickEdit = { onClickEditExperience(experience.id) },
                        onClickDelete = { onClickDeleteExperience(experience.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExperienceSuccessUIPreview() {
    LogitTheme {
        ExperienceSuccessUI(
            experiences = listOf(
                Experience(
                    id = "preview-id",
                    tags = listOf("소통력", "협력적소통", "협력적소통"),
                    situation = "서비스 초기 단계에서 데이터 기반 개선이 필요했습니다.",
                    task = "핵심 전환 지표를 2주 안에 개선하는 목표를 세웠습니다.",
                    action = "퍼널 분석, A/B 테스트, 카피 개선을 반복하며 우선순위를 조정했습니다.",
                    result = "전환율 35% 개선과 이탈률 감소를 달성했습니다.",
                    category = com.useai.core.model.experience.ExperienceCategory.COLLABORATIVE_COMMUNICATION,
                    startDate = LocalDate.of(2022, 4, 6),
                    endDate = LocalDate.of(2022, 4, 6),
                    experienceType = ExperienceType.Intern,
                    formatType = ExperienceCreateFormatType.STAR,
                    title = "로짓 데이터 분석을 통한 이탈률 개선"
                ),
            ),
            openedMenuExperienceId = null,
            isDeleting = false,
            onClickAdd = {},
            onClickRegister = {},
            onClickExperienceCard = {},
            onClickExperienceMore = {},
            onDismissMenu = {},
            onClickEditExperience = {},
            showDeleteDialog = false,
            onDeleteConfirm = {},
            onDeleteCancel = {},
            onClickDeleteExperience = {},
        )
    }
}

package com.useai.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitExperienceBanner(
    items: List<ExperienceBannerItem>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { items.size })

    Box(
        modifier = modifier
            .height(155.dp)
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 20.dp,
        ) { page ->
            BannerContent(item = items[page])
        }
        Column(
            modifier = Modifier
                .padding(20.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(
                        vertical = 3.dp,
                        horizontal = 17.dp,
                    ),
            ) {
                Row {
                    Text(
                        text = (pagerState.currentPage + 1).toString(),
                        style = LogitTheme.typography.body7_1,
                        color = LogitTheme.colors.white,
                    )
                    Text(
                        text = " / ${pagerState.pageCount}",
                        style = LogitTheme.typography.body7_4,
                        color = LogitTheme.colors.white
                    )
                }
            }
        }
    }
}

@Composable
private fun BannerContent(
    item: ExperienceBannerItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = item.experienceType.backgroundColors,
                    start = Offset(Float.POSITIVE_INFINITY, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .padding(20.dp),
    ) {
        Column {
            Text(
                text = item.experienceType.title,
                style = LogitTheme.typography.body3_2,
                color = LogitTheme.colors.primary600
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(
                    R.string.home_experience_type_banner_desc_format,
                    item.experienceCount
                ),
                style = LogitTheme.typography.body6_2,
                color = Color(0xFF879BA2)
            )
        }
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(item.experienceType.image),
            contentDescription = item.experienceType.title,
        )
    }
}

data class ExperienceBannerItem(
    val experienceType: ExperienceType,
    val experienceCount: Int,
)

enum class ExperienceType(
    val title: String,
    @get:DrawableRes val image: Int,
    val backgroundColors: List<Color>,
) {
    Leadership(
        title = "주도적 실행력",
        image = R.drawable.ic_leadership_3d,
        backgroundColors = listOf(
            Color(0xFFE0F6FF),
            Color(0xFFE7FCFC),
            Color(0xFFEBFFF4),
        ),
    ),
    Expertise(
        title = "기술적 전문성",
        image = R.drawable.ic_expertise_3d,
        backgroundColors = listOf(
            Color(0xFFE0E7FF),
            Color(0xFFE9F5FF),
            Color(0xFFE5F9F9),
        ),
    ),
    Analysis(
        title = "논리적 분석력",
        image = R.drawable.ic_analysis_3d,
        backgroundColors = listOf(
            Color(0xFFE4E0FF),
            Color(0xFFE9EDFF),
            Color(0xFFE5F3F9),
        ),
    ),
    Creativity(
        title = "창의적 문제해결",
        image = R.drawable.ic_creativity_3d,
        backgroundColors = listOf(
            Color(0xFFEFE0FF),
            Color(0xFFEDE9FF),
            Color(0xFFE5ECF9),
        ),
    ),
    Communication(
        title = "협력적 소통",
        image = R.drawable.ic_communication_3d,
        backgroundColors = listOf(
            Color(0xFFFBEEFE),
            Color(0xFFF4E9FF),
            Color(0xFFEAE6FF),
        ),
    ),
    Accountability(
        title = "끈기 있는 책임감",
        image = R.drawable.ic_accountability_3d,
        backgroundColors = listOf(
            Color(0xFFFFDFFB),
            Color(0xFFEFE2F2),
            Color(0xFFF4E1FF),
        ),
    ),
    Adaptability(
        title = "유연한 적응력",
        image = R.drawable.ic_adaptivity_3d,
        backgroundColors = listOf(
            Color(0xFFFFDEF4),
            Color(0xFFFFF2FD),
            Color(0xFFFFE9FE),
        ),
    ),
    CustomerFocus(
        title = "고객 가치 지향",
        image = R.drawable.ic_customer_focus_3d,
        backgroundColors = listOf(
            Color(0xFFFFE5E0),
            Color(0xFFF8EDEF),
            Color(0xFFFFEAF3),
        )
    )
}

@Preview
@Composable
private fun LogitExperienceBannerPreview() {
    LogitTheme {
        LogitExperienceBanner(
            items = listOf(
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
            )
        )
    }
}

package com.useai.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitExperienceBanner(
    items: List<ExperienceType>,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val totalPageCount = if (items.size > 1) Int.MAX_VALUE else items.size
    val pagerState = rememberPagerState(
        pageCount = { totalPageCount },
        initialPage = if (items.size > 1) (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size) else 0,
    )

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 20.dp,
            contentPadding = PaddingValues(
                horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
            ),
            beyondViewportPageCount = 1,
        ) { page ->
            val index = page % items.size
            BannerContent(type = items[index])
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(vertical = 19.dp, horizontal = 40.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 3.dp, horizontal = 17.dp),
            ) {
                Text(
                    text = (pagerState.currentPage % items.size + 1).toString(),
                    style = LogitTheme.typography.body7_1,
                    color = LogitTheme.colors.white,
                )
                Text(
                    text = " / ${items.size}",
                    style = LogitTheme.typography.body7_4,
                    color = LogitTheme.colors.white,
                )
            }
        }
    }
}

@Composable
private fun BannerContent(
    type: ExperienceType,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(type.bannerImage),
        contentDescription = type.title,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(LogitTheme.colors.white),
        contentScale = ContentScale.Fit,
    )
}

enum class ExperienceType(
    val title: String,
    @get:DrawableRes val bannerImage: Int,
) {
    Leadership(
        title = "주도적 실행력",
        bannerImage = R.drawable.banner_execution,
    ),
    Expertise(
        title = "기술적 전문성",
        bannerImage = R.drawable.banner_expertise,
    ),
    Analysis(
        title = "논리적 분석력",
        bannerImage = R.drawable.banner_analysis,
    ),
    Creativity(
        title = "창의적 문제해결",
        bannerImage = R.drawable.banner_problem_solving,
    ),
    Communication(
        title = "협력적 소통",
        bannerImage = R.drawable.banner_communication,
    ),
    Accountability(
        title = "끈기 있는 책임감",
        bannerImage = R.drawable.banner_responsibility,
    ),
    Adaptability(
        title = "유연한 적응력",
        bannerImage = R.drawable.banner_adaptability,
    ),
    CustomerFocus(
        title = "고객 가치 지향",
        bannerImage = R.drawable.banner_value_orientation,
    ),
}

@Preview
@Composable
private fun LogitExperienceBannerPreview() {
    LogitTheme {
        LogitExperienceBanner(
            items = listOf(
                ExperienceType.Leadership,
                ExperienceType.Expertise,
                ExperienceType.Analysis,
                ExperienceType.Creativity,
            ),
        )
    }
}

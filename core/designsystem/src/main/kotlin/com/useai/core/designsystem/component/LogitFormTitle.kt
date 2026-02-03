package com.useai.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitFormTitle(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            color = LogitTheme.colors.black,
            style = LogitTheme.typography.body3_1,
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_title_to_desc))
        )
        Text(
            text = desc,
            color = LogitTheme.colors.gray300,
            style = LogitTheme.typography.body6_1,
        )
    }
}

@Preview
@Composable
private fun LogitFormTitlePreview() {
    LogitTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LogitTheme.colors.white)
        ) {
            LogitFormTitle(
                title = "자기소개서 작성",
                desc = "지원하는 기업의 정보를 알려주세요"
            )
        }
    }
}

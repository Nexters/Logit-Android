package com.useai.core.designsystem.component.appbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.core.designsystem.theme.LogitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpTitle(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = LogitTheme.typography.body4
            )
        },
        // 기본 inset을 0으로 설정하여 중첩 패딩 방지
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.height(54.dp),
        navigationIcon = {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    painter = painterResource(LogitIcons.NavigateLeft),
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LogitTheme.colors.white,
            titleContentColor = LogitTheme.colors.black,
            navigationIconContentColor = LogitTheme.colors.black
        )
    )
}

@Preview
@Composable
private fun PopUpTitlePreview() {
    PopUpTitle(
        title = "네이버 프론트엔드 개발",
        onClick = {}
    )
}

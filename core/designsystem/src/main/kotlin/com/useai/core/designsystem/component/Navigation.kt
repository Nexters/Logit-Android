package com.useai.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .background(LogitTheme.colors.white)
            .navigationBarsPadding()
            .height(63.dp),
        containerColor = LogitTheme.colors.white
    ) {
        content()
    }
}

@Composable
fun RowScope.LogitNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    labelText: String = "",
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = labelText,
                // AS-IS: (icon 영역 기본 padding 4dp) + (icon과 label 사이의 기본 간격 4dp) = 8dp
                // TO-BE: 4dp
                modifier = Modifier.offset(y = (-4).dp),
                style = LogitTheme.typography.label1
            )
        },
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = LogitTheme.colors.secondary100,
            selectedTextColor = LogitTheme.colors.black,
            unselectedIconColor = LogitTheme.colors.gray300,
            unselectedTextColor = LogitTheme.colors.primary400,
            indicatorColor = Color.Transparent,
        ),
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
private fun LogitNavigationBarPreview() {
    val items = listOf("홈", "자소서", "작성", "경험", "리포트")
    val icons = listOf(
        LogitIcons.HomeDefault,
        LogitIcons.PaperDefault,
        LogitIcons.AddDefault,
        LogitIcons.ExperienceDefault,
        LogitIcons.ReportDefault,
    )
    val selectedIcons = listOf(
        LogitIcons.HomeSelected,
        LogitIcons.PaperSelected,
        LogitIcons.AddSelected,
        LogitIcons.ExperienceSelected,
        LogitIcons.ReportSelected,
    )

    LogitTheme {
        LogitNavigationBar(
            modifier = Modifier
        ) {
            items.forEachIndexed { index, item ->
                LogitNavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(icons[index]),
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            painter = painterResource(selectedIcons[index]),
                            contentDescription = item,
                        )
                    },
                    labelText = item,
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}

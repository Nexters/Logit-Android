package com.useai.logit.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.feature.add.AddScreen
import com.useai.feature.chat.ChatScreen
import com.useai.feature.experience.ExperienceScreen
import com.useai.feature.home.HomeScreen
import com.useai.feature.report.ReportScreen
import com.useai.feature.add.R as addR
import com.useai.feature.chat.R as chatR
import com.useai.feature.experience.R as experienceR
import com.useai.feature.home.R as homeR
import com.useai.feature.report.R as reportR

data class TopLevelNavItem(
    @get:DrawableRes val selectedIconId: Int,
    @get:DrawableRes val unselectedIconId: Int,
    @get:StringRes val iconTextId: Int,
    @get:StringRes val titleTextId: Int,
) {
    companion object {
        fun fromScreen(screen: Screen): TopLevelNavItem? {
            return when (screen) {
                is HomeScreen -> HOME
                is ChatScreen -> PAPER
                is AddScreen -> ADD
                is ExperienceScreen -> EXPERIENCE
                is ReportScreen -> REPORT
                else -> null
            }
        }
    }
}

val HOME = TopLevelNavItem(
    selectedIconId = LogitIcons.HomeSelected,
    unselectedIconId = LogitIcons.HomeDefault,
    iconTextId = homeR.string.feature_home_title,
    titleTextId = homeR.string.feature_home_title,
)

// TODO: 자소서 화면 이름 통일
val PAPER = TopLevelNavItem(
    selectedIconId = LogitIcons.PaperSelected,
    unselectedIconId = LogitIcons.PaperDefault,
    iconTextId = chatR.string.feature_chat_title,
    titleTextId = chatR.string.feature_chat_title,
)

val ADD = TopLevelNavItem(
    selectedIconId = LogitIcons.AddSelected,
    unselectedIconId = LogitIcons.AddDefault,
    iconTextId = addR.string.feature_add_title,
    titleTextId = addR.string.feature_add_title,
)

val EXPERIENCE = TopLevelNavItem(
    selectedIconId = LogitIcons.ExperienceSelected,
    unselectedIconId = LogitIcons.ExperienceDefault,
    iconTextId = experienceR.string.feature_experience_title,
    titleTextId = experienceR.string.feature_experience_title,
)

val REPORT = TopLevelNavItem(
    selectedIconId = LogitIcons.ReportSelected,
    unselectedIconId = LogitIcons.ReportDefault,
    iconTextId = reportR.string.feature_report_title,
    titleTextId = reportR.string.feature_report_title,
)

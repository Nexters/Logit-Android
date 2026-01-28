package com.useai.logit.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.designsystem.R
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.feature.chat.ChatScreen
import com.useai.feature.experience.ExperienceScreen
import com.useai.feature.home.HomeScreen
import com.useai.feature.newproject.NewProjectScreen
import com.useai.feature.report.ReportScreen

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
                is ChatScreen -> COVER_LETTER
                is NewProjectScreen -> NEW_PROJECT
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
    iconTextId = R.string.home_title,
    titleTextId = R.string.home_title,
)

val COVER_LETTER = TopLevelNavItem(
    selectedIconId = LogitIcons.PaperSelected,
    unselectedIconId = LogitIcons.PaperDefault,
    iconTextId = R.string.cover_letter_title,
    titleTextId = R.string.cover_letter_title,
)

val NEW_PROJECT = TopLevelNavItem(
    selectedIconId = LogitIcons.AddSelected,
    unselectedIconId = LogitIcons.AddDefault,
    iconTextId = R.string.new_project_title,
    titleTextId = R.string.new_project_title,
)

val EXPERIENCE = TopLevelNavItem(
    selectedIconId = LogitIcons.ExperienceSelected,
    unselectedIconId = LogitIcons.ExperienceDefault,
    iconTextId = R.string.experience_title,
    titleTextId = R.string.experience_title,
)

val REPORT = TopLevelNavItem(
    selectedIconId = LogitIcons.ReportSelected,
    unselectedIconId = LogitIcons.ReportDefault,
    iconTextId = R.string.report_title,
    titleTextId = R.string.report_title,
)

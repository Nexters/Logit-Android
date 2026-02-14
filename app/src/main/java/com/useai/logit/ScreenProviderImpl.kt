package com.useai.logit

import com.slack.circuit.runtime.screen.Screen
import com.useai.core.navigation.ScreenProvider
import com.useai.feature.chat.ChatScreen
import com.useai.feature.experience.ExperienceCreateScreen
import com.useai.feature.experience.ExperienceDetailScreen
import com.useai.feature.newproject.NewProjectBasicInfoScreen
import com.useai.feature.newproject.NewProjectQuestionScreen

class ScreenProviderImpl: ScreenProvider {
    override fun newProjectBasicInfoScreen(): Screen = NewProjectBasicInfoScreen

    override fun newProjectQuestionScreen(
        companyName: String,
        jobName: String,
        jobDesc: String,
        talent: String
    ): Screen = NewProjectQuestionScreen(
        companyName = companyName,
        jobName = jobName,
        jobDesc = jobDesc,
        talent = talent,
    )

    override fun chatScreen(projectId: String): Screen = ChatScreen(projectId)

    override fun experienceCreateScreen(): Screen = ExperienceCreateScreen

    override fun experienceDetailScreen(experienceId: String): Screen = ExperienceDetailScreen(
        experienceId = experienceId
    )
}

package com.useai.core.navigation

import com.slack.circuit.runtime.screen.Screen

interface ScreenProvider {
    fun newProjectBasicInfoScreen(): Screen
    fun newProjectQuestionScreen(
        companyName: String,
        jobName: String,
        jobDesc: String,
        talent: String
    ): Screen
    fun chatScreen(projectId: String): Screen
    fun experienceCreateScreen(): Screen
}

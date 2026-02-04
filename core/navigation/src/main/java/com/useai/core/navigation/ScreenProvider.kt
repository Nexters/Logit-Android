package com.useai.core.navigation

import com.slack.circuit.runtime.screen.Screen

interface ScreenProvider {
    fun chatScreen(projectId: Int): Screen
    fun newProjectBasicInfoScreen(): Screen
    fun newProjectQuestionScreen(
        companyName: String,
        jobName: String,
        jobDesc: String,
        talent: String
    ): Screen
}

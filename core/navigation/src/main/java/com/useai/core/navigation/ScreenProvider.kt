package com.useai.core.navigation

import com.slack.circuit.runtime.screen.Screen

interface ScreenProvider {
    fun rootScreen(): Screen
    fun homeScreen(): Screen
    fun accountScreen(): Screen
    fun projectsScreen(): Screen
    fun newProjectBasicInfoScreen(): Screen
    fun newProjectQuestionScreen(
        companyName: String,
        jobName: String,
        jobDesc: String,
        talent: String
    ): Screen
    fun chatScreen(projectId: String): Screen
    fun newQuestionScreen(projectId: String): Screen
    fun editQuestionsScreen(projectId: String): Screen
    fun experienceCreateScreen(): Screen
    fun experienceDetailScreen(experienceId: String): Screen
}

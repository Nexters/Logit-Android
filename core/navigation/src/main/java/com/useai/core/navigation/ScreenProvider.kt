package com.useai.core.navigation

import com.slack.circuit.runtime.screen.Screen
import java.time.LocalDate

interface ScreenProvider {
    fun onboardingScreen(): Screen
    fun rootScreen(): Screen
    fun homeScreen(): Screen
    fun accountScreen(): Screen
    fun projectsScreen(): Screen
    fun projectLibraryScreen(projectId: String): Screen
    fun newProjectBasicInfoScreen(): Screen
    fun newProjectQuestionScreen(
        companyName: String,
        jobName: String,
        jobDesc: String,
        talent: String,
        dueDate: LocalDate,
    ): Screen
    fun chatScreen(projectId: String): Screen
    fun newQuestionScreen(projectId: String): Screen
    fun editQuestionsScreen(projectId: String): Screen
    fun experienceCreateScreen(experienceId: String? = null): Screen
    fun experienceDetailScreen(experienceId: String): Screen
}

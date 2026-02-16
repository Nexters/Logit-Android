package com.useai.feature.home

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.model.project.ProjectListItem
import com.useai.core.ui.ExperienceBannerItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val bannerItems: List<ExperienceBannerItem>,
        val projects: List<ProjectListItem>,
    ) : CircuitUiState
}

class HomePresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<HomeScreen.State> {
    @Composable
    override fun present(): HomeScreen.State {
        return HomeScreen.State(
            bannerItems = emptyList(),
            projects = emptyList() // TODO: api 응답 값 사용
        )
    }

    @AssistedFactory
    @CircuitInject(HomeScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): HomePresenter
    }
}

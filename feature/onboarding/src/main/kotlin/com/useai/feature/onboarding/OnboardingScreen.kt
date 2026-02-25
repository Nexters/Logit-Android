package com.useai.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize

@Parcelize
data object OnboardingScreen : Screen {
    data class State(
        val content: TutorialContent,
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object SkipClicked : Event
        data class NextClicked(
            val currentPage: Int,
        ) : Event
    }
}

enum class TutorialContent(
    val page: Int,
    val iconResId: Int,
    val desc: String,
    val highlightText: String,
    val screenImageResId: Int,
    val buttonText: String,
) {
    Example(
        page = 1,
        iconResId = R.drawable.ic_onboarding_example,
        desc = "예시 불러오기로 빠르게 성능을\n테스트 해보세요",
        highlightText = "예시 불러오기",
        screenImageResId = R.drawable.img_onboarding_example,
        buttonText = "다음으로",
    ),
    Experience(
        page = 2,
        iconResId = R.drawable.ic_onboarding_experience,
        desc = "등록한 경험과 작성하는\n공고간의 매칭 점수를 확인해보세요",
        highlightText = "공고간의 매칭 점수",
        screenImageResId = R.drawable.img_onboarding_experience,
        buttonText = "다음으로",
    ),
    Report(
        page = 3,
        iconResId = R.drawable.ic_onboarding_report,
        desc = "리포트로 나의 강점을 파악해보세요",
        highlightText = "리포트",
        screenImageResId = R.drawable.img_onboarding_report,
        buttonText = "시작하기",
    ),
    ;

    @Composable
    fun getStyledDescription(): AnnotatedString {
        val fullText = desc
        val startIndex = fullText.indexOf(highlightText)

        return buildAnnotatedString {
            if (startIndex != -1) {
                // 강조 텍스트 앞부분
                withStyle(
                    style = LogitTheme.typography.body3_3.toSpanStyle()
                        .copy(color = LogitTheme.colors.black)
                ) {
                    append(fullText.take(startIndex))
                }

                // 강조 텍스트 (색상 및 폰트 적용)
                withStyle(
                    style = LogitTheme.typography.body3_2.toSpanStyle()
                        .copy(color = LogitTheme.colors.primary100)
                ) {
                    append(highlightText)
                }

                // 강조 텍스트 뒷부분
                withStyle(
                    style = LogitTheme.typography.body3_3.toSpanStyle()
                        .copy(color = LogitTheme.colors.black)
                ) {
                    append(fullText.substring(startIndex + highlightText.length))
                }
            } else {
                append(fullText)
            }
        }
    }
}

class OnboardingPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<OnboardingScreen.State> {
    @Composable
    override fun present(): OnboardingScreen.State {
        var content by rememberRetained {
            mutableStateOf(TutorialContent.Example)
        }
        val screenProvider = LocalScreenProvider.current
        val eventSink: (OnboardingScreen.Event) -> Unit = { event: OnboardingScreen.Event ->
            when (event) {
                OnboardingScreen.Event.SkipClicked -> {
                    navigator.goTo(screenProvider.rootScreen())
                }

                is OnboardingScreen.Event.NextClicked -> {
                    val currentPage = event.currentPage
                    if (currentPage == TutorialContent.entries.size) {
                        navigator.resetRoot(screenProvider.rootScreen())
                    } else {
                        content = TutorialContent.entries[currentPage]
                    }
                }
            }
        }

        return OnboardingScreen.State(
            content = content,
            eventSink = eventSink,
        )
    }

    @AssistedFactory
    @CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): OnboardingPresenter
    }
}

package com.useai.feature.newproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.navigation.LocalScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data object NewProjectBasicInfoScreen : Screen {
    data class State(
        val companyName: String,
        val jobName: String,
        val jobDesc: String,
        val talent: String,
        val dueDate: LocalDate?,
        val isAlwaysOpen: Boolean,
        val showExitDialog: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object Back : Event
        data object LoadExample : Event
        data class OnCompanyNameChange(val name: String) : Event
        data class OnJobNameChange(val job: String) : Event
        data class OnJobDescChange(val desc: String) : Event
        data class OnTalentChange(val talent: String) : Event
        data class OnDueDateChange(val date: LocalDate?) : Event
        data class OnAlwaysOpenChange(val isChecked: Boolean) : Event
        data object Next : Event
        data object DismissExitDialog : Event
        data object ConfirmExit : Event
    }
}

class NewProjectBasicInfoPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<NewProjectBasicInfoScreen.State> {
    @Composable
    override fun present(): NewProjectBasicInfoScreen.State {
        var companyName by rememberRetained { mutableStateOf("") }
        var jobName by rememberRetained { mutableStateOf("") }
        var jobDesc by rememberRetained { mutableStateOf("") }
        var talent by rememberRetained { mutableStateOf("") }
        var dueDate by rememberRetained { mutableStateOf<LocalDate?>(null) }
        var isAlwaysOpen by rememberRetained { mutableStateOf(false) }
        var showExitDialog by rememberRetained { mutableStateOf(false) }
        val screenProvider = LocalScreenProvider.current

        return NewProjectBasicInfoScreen.State(
            companyName = companyName,
            jobName = jobName,
            jobDesc = jobDesc,
            talent = talent,
            dueDate = dueDate,
            isAlwaysOpen = isAlwaysOpen,
            showExitDialog = showExitDialog,
        ) { event ->
            when (event) {
                is NewProjectBasicInfoScreen.Event.Back -> showExitDialog = true
                is NewProjectBasicInfoScreen.Event.LoadExample -> {
                    companyName = SAMPLE_COMPANY_NAME
                    jobName = SAMPLE_JOB_NAME
                    jobDesc = SAMPLE_JOB_DESC
                    talent = SAMPLE_COMPANY_TALENT
                    dueDate = LocalDate.now().plusDays(14)
                    isAlwaysOpen = false
                }
                is NewProjectBasicInfoScreen.Event.OnCompanyNameChange -> companyName = event.name
                is NewProjectBasicInfoScreen.Event.OnJobNameChange -> jobName = event.job
                is NewProjectBasicInfoScreen.Event.OnJobDescChange -> jobDesc = event.desc
                is NewProjectBasicInfoScreen.Event.OnTalentChange -> talent = event.talent
                is NewProjectBasicInfoScreen.Event.OnDueDateChange -> dueDate = event.date
                is NewProjectBasicInfoScreen.Event.OnAlwaysOpenChange -> {
                    isAlwaysOpen = event.isChecked
                    if (event.isChecked) {
                        dueDate = null
                    }
                }
                is NewProjectBasicInfoScreen.Event.Next -> {
                    if (isAlwaysOpen || dueDate != null) {
                        val resolvedDueDate = if (isAlwaysOpen) LocalDate.MAX else dueDate!!
                        val newProjectQuestionScreen = screenProvider.newProjectQuestionScreen(
                            companyName = companyName,
                            jobName = jobName,
                            jobDesc = jobDesc,
                            talent = talent,
                            dueDate = resolvedDueDate,
                        )
                        navigator.goTo(newProjectQuestionScreen)
                    }
                }
                is NewProjectBasicInfoScreen.Event.DismissExitDialog -> showExitDialog = false
                is NewProjectBasicInfoScreen.Event.ConfirmExit -> {
                    showExitDialog = false
                    navigator.pop()
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(NewProjectBasicInfoScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): NewProjectBasicInfoPresenter
    }

    companion object {
        private const val SAMPLE_COMPANY_NAME = "주식회사 로짓 (Logit Inc.)"
        private const val SAMPLE_JOB_NAME = "서비스 기획자 (Product Manager) - 신입/경력"
        private const val SAMPLE_JOB_DESC = """[주요 업무]
로짓의 핵심 기능인 RAG 기반 자소서 초안 생성 엔진 고도화 및 매칭 로직 기획
사용자의 커리어 자산을 분석하여 역량 변화를 보여주는 '대시보드 기획'
사용자 여정(User Journey) 분석을 통한 자소서 작성 진입장벽 최소화 및 UT 기반 UX 개선

[자격 요건]
사용자의 불편함을 데이터나 인터뷰로 포착하여 실질적인 솔루션(Action)으로 해결해본 경험이 있는 분
복잡한 요구사항을 논리적으로 구조화하여 개발자/디자이너와 명확하게 소통할 수 있는 분
AI 기술(LLM, RAG 등)의 비즈니스적 가치와 한계를 이해하고 이를 서비스에 녹여내고 싶은 분

[우대 사항]
취업 준비생을 위한 커뮤니티나 서비스를 직접 운영하거나 깊게 경험해 보신 분
데이터 시각화 도구를 활용해 복잡한 정보를 직관적인 리포트로 구성해 본 경험이 있는 분"""
        private const val SAMPLE_COMPANY_TALENT = """전략적 통찰 (Strategic Insight): 단순한 나열이 아닌, 데이터 사이의 숨겨진 맥락을 읽고 최적의 매칭을 찾아내는 인재
정직한 논리 (Honest Logic): 꾸며낸 말이 아닌, 실제 팩트와 근거를 바탕으로 설득력 있는 결과물을 만드는 인재
사용자 공감 (User Empathy): 취업 준비의 막막함을 깊이 이해하고, 복잡한 절차를 기술로 혁신하여 극강의 편의성을 추구하는 인재"""
    }
}

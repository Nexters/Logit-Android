package com.useai.feature.newproject.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.LogitFormTitle
import com.useai.core.designsystem.component.LogitInputField
import com.useai.core.designsystem.component.appbar.PopUpTitle
import com.useai.core.designsystem.component.button.LogitCtaButton
import com.useai.core.designsystem.component.stepper.LogitStepper
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.newproject.NewProjectBasicInfoScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@CircuitInject(NewProjectBasicInfoScreen::class, ActivityRetainedComponent::class)
fun NewProjectBasicInfo(
    state: NewProjectBasicInfoScreen.State,
    modifier: Modifier = Modifier
) {
    var companyName by rememberRetained { mutableStateOf("") }
    var jobName by rememberRetained { mutableStateOf("") }
    var jobDesc by rememberRetained { mutableStateOf("") }
    var talent by rememberRetained { mutableStateOf("") }

    val isButtonEnabled = companyName.isNotBlank() && jobName.isNotBlank() && jobDesc.isNotBlank()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LogitTheme.colors.white,
        topBar = {
            PopUpTitle(
                onClick = {
                    state.eventSink(NewProjectBasicInfoScreen.Event.Back)
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                LogitStepper(
                    currentStep = "1",
                    totalStep = "2"
                )
                Spacer(
                    modifier = Modifier.height(13.dp)
                )
                LogitFormTitle(
                    title = "자기소개서 작성",
                    desc = "지원하는 기업의 정보를 알려주세요"
                )
                Spacer(
                    modifier = Modifier.height(41.dp)
                )
                LogitInputField(
                    label = "기업명",
                    isRequired = true,
                    maxLength = "100",
                    input = companyName,
                    onInputChange = { companyName = it },
                    placeHolder = "예) 로짓 컴퍼니",
                    maxLines = 1,
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(R.dimen.space_between_fields))
                )
                LogitInputField(
                    label = "직무명",
                    isRequired = true,
                    maxLength = "100",
                    input = jobName,
                    onInputChange = { jobName = it },
                    placeHolder = "예) 프로덕트 디자이너",
                    maxLines = 1,
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(R.dimen.space_between_fields))
                )
                LogitInputField(
                    label = "채용 공고",
                    isRequired = true,
                    maxLength = "3000",
                    input = jobDesc,
                    onInputChange = { jobDesc = it },
                    placeHolder = "주요 업무, 자격 요건, 우대 사항 등을 입력하세요",
                    maxLines = 4,
                    minLines = 4,
                )
                Spacer(
                    modifier = Modifier.height(dimensionResource(R.dimen.space_between_fields))
                )
                LogitInputField(
                    label = "기업 인재상",
                    isRequired = false,
                    maxLength = "1000",
                    input = talent,
                    onInputChange = { talent = it },
                    placeHolder = "기업의 인재상이나 핵심가치를 입력하세요",
                    maxLines = 4,
                )
                Spacer(modifier = Modifier.height(100.dp))
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = LogitTheme.colors.white,
            ) {
                LogitCtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    text = "다음으로",
                    onClick = {
                        state.eventSink(
                            NewProjectBasicInfoScreen.Event.Next(
                                companyName,
                                jobName
                            )
                        )
                    },
                    enabled = isButtonEnabled
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewProjectBasicInfoPreview() {
    LogitTheme {
        NewProjectBasicInfo(
            state = NewProjectBasicInfoScreen.State { }
        )
    }
}

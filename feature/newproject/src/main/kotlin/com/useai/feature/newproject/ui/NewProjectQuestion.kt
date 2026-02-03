package com.useai.feature.newproject.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.useai.core.designsystem.component.LogitFormTitle
import com.useai.core.designsystem.component.appbar.PopUpTitle
import com.useai.core.designsystem.component.button.LogitAddButton
import com.useai.core.designsystem.component.button.LogitCtaButton
import com.useai.core.designsystem.component.container.LogitOutlinedContainer
import com.useai.core.designsystem.component.stepper.LogitStepper
import com.useai.core.designsystem.component.textfield.LogitOutlinedTextField
import com.useai.core.designsystem.icon.LogitIcons
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.newproject.NewProjectQuestionScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(NewProjectQuestionScreen::class, ActivityRetainedComponent::class)
fun NewProjectQuestion(
    modifier: Modifier = Modifier,
    state: NewProjectQuestionScreen.State,
) {
    var questions by rememberRetained { mutableStateOf(listOf(""))}
    val isButtonEnabled = questions.isNotEmpty()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LogitTheme.colors.white,
        topBar = {
            PopUpTitle(
                onClick = {
                    state.eventSink(NewProjectQuestionScreen.Event.Back) // TODO: 앞 페이지로 이동
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
                    currentStep = "2",
                    totalStep = "2"
                )
                Spacer(
                    modifier = Modifier.height(13.dp)
                )
                LogitFormTitle(
                    title = "자기소개서 문항 입력",
                    desc = "자기소개서의 문항을 입력해주세요"
                )
                Spacer(
                    modifier = Modifier.height(75.dp)
                )
                questions.forEachIndexed { i, question ->
                    val placeHolder = if (question.isEmpty()) "${i + 1}번째 문항" else ""
                    NewQuestion(
                        onValueChange = {
                            val newQuestions = questions.toMutableList()
                            newQuestions[i] = it
                            questions = newQuestions
                        },
                        placeHolder = placeHolder,
                        isAdditionalQuestion = i > 0,
                    )
                    Spacer(Modifier.height(8.dp))
                }
                LogitAddButton(
                    onClick = {
                        questions = questions + ""
                    }
                )
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
                    text = "프로젝트 생성",
                    onClick = { /* TODO */ },
                    enabled = isButtonEnabled
                )
            }
        }
    }
}

@Composable
private fun NewQuestion(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    placeHolder: String = "",
    isAdditionalQuestion: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LogitOutlinedTextField(
            value = "",
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .align(Alignment.CenterVertically),
            placeholder = placeHolder,
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
        )
        Spacer(Modifier.width(12.dp))
        LetterCountField(
            modifier = Modifier
                .fillMaxHeight()
                .width(86.dp),
        )
        if (isAdditionalQuestion) {
            Spacer(Modifier.width(12.dp))
            DeleteButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = {
                    onValueChange("") // TODO: 삭제
                },
            )
        }
    }
}

@Composable
private fun LetterCountField(
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }

    LogitOutlinedContainer(
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                modifier = Modifier
                    .width(39.dp),
                value = text,
                onValueChange = { text = it },
                textStyle = LogitTheme.typography.body6_1,
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (text.isEmpty()) {
                            Text(
                                text = "글자수",
                                style = LogitTheme.typography.body6_1,
                                color = LogitTheme.colors.gray200,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = "자",
                style = LogitTheme.typography.body6_1,
                color = LogitTheme.colors.primary500,
            )
        }
    }
}

@Composable
private fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    LogitOutlinedContainer(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = LogitIcons.Trash),
                contentDescription = "삭제",
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun NewProjectQuestionPreview() {
    NewProjectQuestion(
        state = NewProjectQuestionScreen.State { }
    )
}

@Preview
@Composable
private fun AdditionalQuestionPreview() {
    LogitTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LogitTheme.colors.white)
        ) {
            NewQuestion(
                isAdditionalQuestion = true,
            )
        }
    }
}

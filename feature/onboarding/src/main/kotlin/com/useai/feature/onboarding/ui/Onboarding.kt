package com.useai.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.ui.LogitCtaButton
import com.useai.core.ui.LogitStepper
import com.useai.feature.onboarding.OnboardingScreen
import com.useai.feature.onboarding.TutorialContent
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(OnboardingScreen::class, ActivityRetainedComponent::class)
fun Onboarding(
    modifier: Modifier = Modifier,
    state: OnboardingScreen.State,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LogitTheme.colors.white,
        // 내비바와 키보드 padding이 중복 계산되어 여백이 생기는 문제 방지
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.union(WindowInsets.ime),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(17.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LogitStepper(
                        currentStep = state.content.page.toString(),
                        totalStep = TutorialContent.entries.size.toString(),
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.onboarding_skip),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable(
                                onClick = {
                                    state.eventSink(OnboardingScreen.Event.SkipClicked)
                                }
                            ),
                        style = LogitTheme.typography.body3_3,
                        color = LogitTheme.colors.gray200,
                    )
                }
                Spacer(Modifier.height(34.dp))
                Image(
                    painter = painterResource(state.content.iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(13.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = state.content.getStyledDescription(),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                    )
                }
                Image(
                    painter = painterResource(state.content.screenImageResId),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = dimensionResource(R.dimen.spacing_cta_button_area_vertical)
                    ),
                color = LogitTheme.colors.white,
            ) {
                LogitCtaButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.content.buttonText,
                    onClick = {
                        state.eventSink(
                            OnboardingScreen.Event.NextClicked(state.content.page)
                        )
                    },
                    enabled = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingPreview() {
    LogitTheme {
        Onboarding(
            state = OnboardingScreen.State(
                content = TutorialContent.Example,
            ),
        )
    }
}

package com.useai.feature.account.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.account.LoginScreen
import dagger.hilt.android.components.ActivityRetainedComponent

@Composable
@CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
fun Login(
    modifier: Modifier = Modifier,
    state: LoginScreen.State,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = LogitTheme.colors.white,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(paddingValues)
                .padding(
                    horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                    vertical = 8.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(294.dp))
            Image(
                painter = painterResource(R.drawable.ic_app_logo_symbol_3d),
                contentDescription = stringResource(R.string.content_description_app_logo),
                modifier = Modifier.size(70.dp),
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_launcher_heading_vertical)))
            Image(
                painter = painterResource(R.drawable.ic_app_logo_wordmark),
                contentDescription = stringResource(R.string.content_description_app_logo),
                modifier = Modifier.size(width = 71.dp, height = 36.dp),
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_launcher_heading_vertical)))
            Text(
                text = stringResource(R.string.launcher_phrase),
                style = LogitTheme.typography.body3_2,
                color = LogitTheme.colors.gray100,
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    state.eventSink(LoginScreen.Event.GoogleLoginClicked)
                },
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonColors(
                    containerColor = LogitTheme.colors.white,
                    contentColor = LogitTheme.colors.black,
                    disabledContainerColor = LogitTheme.colors.white,
                    disabledContentColor = LogitTheme.colors.gray70,
                ),
                border = BorderStroke(1.dp, LogitTheme.colors.gray100),
                contentPadding = PaddingValues(
                    vertical = 10.dp,
                    horizontal = 22.dp,
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Google로 시작하기",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = LogitTheme.typography.body6_1,
                        color = LogitTheme.colors.black,
                    )
                    Spacer(Modifier.width(32.dp))
                }
            }
            Spacer(Modifier.height(38.dp))
            Text(
                text = getTermsAndPolicyString(
                    onTermsClick = {
                        state.eventSink(LoginScreen.Event.TermsClicked)
                    },
                    onPrivacyClick = {
                        state.eventSink(LoginScreen.Event.PrivacyClicked)
                    },
                ),
                style = LogitTheme.typography.body9_3,
                color = LogitTheme.colors.black,
            )
        }
    }
}

@Composable
private fun getTermsAndPolicyString(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
): AnnotatedString = buildAnnotatedString {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = LogitTheme.colors.primary100,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
        )
    )

    append("계속하면 ")
    withLink(
        LinkAnnotation.Clickable(
            tag = "TERMS",
            linkInteractionListener = {
                onTermsClick()
            },
            styles = linkStyles,
        )
    ) {
        append("이용약관")
    }
    append(" · ")
    withLink(
        LinkAnnotation.Clickable(
            tag = "PRIVACY",
            linkInteractionListener = {
                onPrivacyClick()
            },
            styles = linkStyles,
        )
    ) {
        append("개인정보 처리방침")
    }
    append("에 동의합니다.")
}

@Preview
@Composable
private fun LoginPreview() {
    LogitTheme {
        Login(
            state = LoginScreen.State(),
        )
    }
}

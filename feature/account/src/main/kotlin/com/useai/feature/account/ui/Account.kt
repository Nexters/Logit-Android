package com.useai.feature.account.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.appbar.PopUpTitle
import com.useai.core.designsystem.component.toggle.LogitSwitch
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.feature.account.AccountScreen
import dagger.hilt.android.components.ActivityRetainedComponent

private const val TAG = "Account"

@Composable
@CircuitInject(AccountScreen::class, ActivityRetainedComponent::class)
fun Account(
    modifier: Modifier = Modifier,
    state: AccountScreen.State,
) {
    BackHandler {
        state.eventSink(AccountScreen.Event.Back)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LogitTheme.colors.white,
        topBar = {
            PopUpTitle(
                onClick = {
                    state.eventSink(AccountScreen.Event.Back)
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            // 사용자 정보
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // TODO: Google 프로필 이미지 표시
                Image(
                    painter = painterResource(R.drawable.ic_app_user),
                    contentDescription = stringResource(R.string.content_description_user_profile),
                    modifier = Modifier.size(48.dp),
                )
                Spacer(Modifier.width(20.dp))
                Text(
                    text = state.userName,
                    style = LogitTheme.typography.body1,
                    color = LogitTheme.colors.gray400,
                )
            }
            Spacer(Modifier.height(26.dp))

            AccountDivider()

            // 알림 설정
            Spacer(Modifier.height(28.dp))
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                )
            ) {
                Text(
                    text = stringResource(R.string.account_notification_settings_title),
                    style = LogitTheme.typography.body5_2,
                    color = LogitTheme.colors.black,
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.account_report_notification_setting),
                        modifier = Modifier.weight(1f),
                        style = LogitTheme.typography.body7_4,
                        color = LogitTheme.colors.gray400,
                    )
                    LogitSwitch(
                        checked = state.reportNotificationEnabled,
                        onCheckedChange = { checked ->
                            // TODO: checked가 항상 true인데 LogitSwitch 내부에서 처리하게 수정되면 AccountScreen 로직 수정 필요
                            Log.d(TAG, "onCheckedChange: $checked")
                            state.eventSink(
                                AccountScreen.Event.ReportNotificationSettingUpdated
                            )
                        }
                    )
                }
            }
            Spacer(Modifier.height(18.dp))

            AccountDivider()

            // 고객 지원 및 정보
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.account_support_and_info_title),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                ),
                style = LogitTheme.typography.body5_2,
                color = LogitTheme.colors.black,
            )
            Spacer(Modifier.height(14.dp))
            SupportItem(
                text = stringResource(R.string.account_contact),
                onClick = {
                    state.eventSink(AccountScreen.Event.Contact)
                }
            )
            SupportItem(
                text = stringResource(R.string.account_logout),
                onClick = {
                    state.eventSink(AccountScreen.Event.Logout)
                }
            )
            SupportItem(
                text = stringResource(R.string.account_withdraw),
                onClick = {
                    state.eventSink(AccountScreen.Event.Withdraw)
                }
            )
        }
    }
}

@Composable
private fun AccountDivider() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = LogitTheme.colors.gray50,
    )
}

@Composable
private fun SupportItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            )
            .padding(
                horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = LogitTheme.typography.body7_4,
            color = LogitTheme.colors.gray400,
        )
        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun AccountPreview() {
    LogitTheme {
        Account(
            state = AccountScreen.State("로짓", false)
        )
    }
}

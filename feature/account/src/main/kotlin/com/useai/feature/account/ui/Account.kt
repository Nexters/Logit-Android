package com.useai.feature.account.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.slack.circuit.codegen.annotations.CircuitInject
import com.useai.core.common.extensions.formatTokenCount
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.appbar.PopUpTitle
import com.useai.core.designsystem.component.toggle.LogitSwitch
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.account.UserProfile
import com.useai.core.ui.LogitDialog
import com.useai.feature.account.AccountScreen
import dagger.hilt.android.components.ActivityRetainedComponent

private const val TAG = "Account"
private const val GUIDE_URL = "https://docs.logit.ai.kr/"
private const val PROFILE_URL = "https://logit.ai.kr/profile"

@Composable
@CircuitInject(AccountScreen::class, ActivityRetainedComponent::class)
fun Account(
    state: AccountScreen.State,
    modifier: Modifier = Modifier,
) {
    var newlyGrantedTokenAmount by remember { mutableIntStateOf(0) }

    LaunchedEffect(state.effects) {
        state.effects.collect { effect ->
            when (effect) {
                is AccountScreen.Effect.TokenGranted -> {
                    newlyGrantedTokenAmount = effect.amount
                    Log.d(
                        TAG,
                        "Token granted: amount=${effect.amount}",
                    )
                }
            }
        }
    }

    BackHandler(enabled = !state.showGuideWebView && !state.showProfileWebView) {
        state.eventSink(AccountScreen.Event.Back)
    }

    if (state.showLogoutDialog) {
        LogitDialog(
            title = stringResource(R.string.account_dialog_logout_title),
            confirmText = stringResource(R.string.account_dialog_logout),
            onConfirm = { state.eventSink(AccountScreen.Event.ConfirmLogout) },
            cancelText = stringResource(R.string.account_dialog_cancel),
            onCancel = { state.eventSink(AccountScreen.Event.DismissLogoutDialog) },
        )
    } else if (state.showWithdrawDialog) {
        LogitDialog(
            title = stringResource(R.string.account_dialog_withdraw_title),
            description = stringResource(R.string.account_dialog_withdraw_desc),
            confirmText = stringResource(R.string.account_dialog_withdraw),
            onConfirm = { state.eventSink(AccountScreen.Event.ConfirmWithdraw) },
            cancelText = stringResource(R.string.account_dialog_cancel),
            onCancel = { state.eventSink(AccountScreen.Event.DismissWithdrawDialog) },
        )
    }

    val horizontalPadding = dimensionResource(R.dimen.screen_common_padding_horizontal)
    val limit = state.tokenLimit.coerceAtLeast(0)
    val balance = state.tokenBalance.coerceIn(0, limit)
    val used = limit - balance
    val progress = if (limit > 0) {
        (used.toFloat() / limit.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    val usagePercent = (progress * 100).toInt()
    val guideMessage = stringResource(R.string.account_guide_page)

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
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
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(LogitTheme.colors.white)
            ) {
                Spacer(Modifier.height(8.dp))
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = state.userProfile.userImageUrl,
                        contentDescription = stringResource(R.string.content_description_user_profile),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(R.drawable.ic_app_user),
                        error = painterResource(R.drawable.ic_app_user),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.width(20.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = state.userProfile.userName.ifBlank { "사용자" },
                                style = LogitTheme.typography.body1,
                                color = LogitTheme.colors.gray400,
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "Free",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 12.sp,
                                ),
                                color = LogitTheme.colors.secondary100,
                                modifier = Modifier
                                    .background(
                                        color = LogitTheme.colors.primary20,
                                        shape = RoundedCornerShape(10.dp),
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = LogitTheme.colors.secondary100,
                                        shape = RoundedCornerShape(10.dp),
                                    )
                                    .padding(horizontal = 12.dp, vertical = 2.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = LogitTheme.colors.gray50,
                )
                Spacer(Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.account_token_usage_title),
                            style = LogitTheme.typography.body5_2,
                            color = LogitTheme.colors.gray400,
                        )
                        if (newlyGrantedTokenAmount > 0) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .height(22.dp)
                                    .background(
                                        color = LogitTheme.colors.primary100,
                                        shape = RoundedCornerShape(50.dp),
                                    )
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "+${newlyGrantedTokenAmount.formatTokenCount()}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LogitTheme.colors.white,
                                    lineHeight = 10.sp,
                                )
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        if (state.isTokenBalanceLoaded) {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                            ) {
                                Text(
                                    text = used.formatTokenCount(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LogitTheme.colors.black,
                                    lineHeight = 14.sp,
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = "/",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LogitTheme.colors.gray300,
                                    lineHeight = 12.sp,
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = limit.formatTokenCount(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LogitTheme.colors.gray300,
                                    lineHeight = 12.sp,
                                )
                            }
                        }
                    }

                    if (state.isTokenBalanceLoaded) {
                        Spacer(Modifier.height(16.dp))

                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(44.dp))
                                .border(
                                    width = 0.89.dp,
                                    color = LogitTheme.colors.primary50,
                                    shape = RoundedCornerShape(44.dp),
                                ),
                        ) {
                            val fillWidth = maxWidth * progress
                            val roundedShape = RoundedCornerShape(44.dp)

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(LogitTheme.colors.gray30),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(fillWidth)
                                    .clip(roundedShape)
                                    .drawBehind {
                                        drawRoundRect(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFFE7F1FF),
                                                    Color(0xFFC3DCFF),
                                                ),
                                            ),
                                            size = size,
                                            cornerRadius = CornerRadius(44.dp.toPx(), 44.dp.toPx()),
                                        )
                                    },
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .drawBehind {
                                        drawRoundRect(
                                            color = Color.Black.copy(alpha = 0.07f),
                                            size = size,
                                            cornerRadius = CornerRadius(44.dp.toPx(), 44.dp.toPx()),
                                        )
                                    },
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = usagePercent.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = LogitTheme.colors.white,
                                    lineHeight = 16.sp,
                                )
                                Text(
                                    text = "%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LogitTheme.colors.white,
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
                AccountDivider()

                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.account_notification_settings_title),
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    style = LogitTheme.typography.body5_2,
                    color = LogitTheme.colors.black,
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.account_report_notification_setting),
                        modifier = Modifier.weight(1f),
                        style = LogitTheme.typography.body7_4,
                        color = LogitTheme.colors.gray400,
                    )
                    LogitSwitch(
                        checked = state.reportNotificationEnabled,
                        onCheckedChange = {
                            state.eventSink(AccountScreen.Event.ReportNotificationSettingUpdated)
                        },
                    )
                }

                Spacer(Modifier.height(16.dp))
                AccountDivider()

                Spacer(Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.account_support_and_info_title),
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                    style = LogitTheme.typography.body5_2,
                    color = LogitTheme.colors.black,
                )
                Spacer(Modifier.height(10.dp))
                SupportItem(
                    text = stringResource(R.string.account_profile),
                    onClick = {
                        state.eventSink(AccountScreen.Event.AccountClicked)
                    },
                )
                SupportItem(
                    text = guideMessage,
                    onClick = {
                        state.eventSink(AccountScreen.Event.GuideClicked)
                    },
                )
                SupportItem(
                    text = stringResource(R.string.account_contact),
                    onClick = {
                        state.eventSink(AccountScreen.Event.Contact)
                    },
                )
                SupportItem(
                    text = stringResource(R.string.account_logout),
                    onClick = {
                        state.eventSink(AccountScreen.Event.LogoutClicked)
                    },
                )
                SupportItem(
                    text = stringResource(R.string.account_withdraw),
                    onClick = {
                        state.eventSink(AccountScreen.Event.WithdrawClicked)
                    },
                )
            }
        }

    }

    if (state.showGuideWebView) {
        AccountWebView(
            url = GUIDE_URL,
            onDismiss = { state.eventSink(AccountScreen.Event.DismissGuideWebView) },
        )
    } else if (state.showProfileWebView) {
        AccountWebView(
            url = PROFILE_URL,
            onDismiss = { state.eventSink(AccountScreen.Event.DismissProfileWebView) },
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun AccountWebView(
    url: String,
    onDismiss: () -> Unit,
) {
    val view = LocalView.current
    val headerColor = LogitTheme.colors.primary20
    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler {
        val currentWebView = webView
        if (currentWebView?.canGoBack() == true) {
            currentWebView.goBack()
        } else {
            onDismiss()
        }
    }

    DisposableEffect(view) {
        val activity = view.context as? Activity
        val insetsController = activity?.window?.let { window ->
            WindowCompat.getInsetsController(window, view)
        }
        val previousLightStatusBars = insetsController?.isAppearanceLightStatusBars

        insetsController?.isAppearanceLightStatusBars = true

        onDispose {
            if (previousLightStatusBars != null) {
                insetsController.isAppearanceLightStatusBars = previousLightStatusBars
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(headerColor),
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            containerColor = headerColor,
            topBar = {
                AccountWebViewHeader(onDismiss = onDismiss)
            },
        ) { paddingValues ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    WebView(context).apply webView@{
                        webView = this
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.userAgentString = settings.userAgentString
                            .replace("; wv", "")
                            .replace("Version/4.0 ", "")
                        CookieManager.getInstance().apply {
                            setAcceptCookie(true)
                            setAcceptThirdPartyCookies(this@webView, true)
                        }
                        webViewClient = WebViewClient()

                        loadUrl(url)
                    }
                },
            )
        }
    }
}

@Composable
private fun AccountWebViewHeader(
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(LogitTheme.colors.primary20),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_app_logo_symbol_word),
            contentDescription = stringResource(R.string.content_description_app_logo),
            modifier = Modifier.size(width = 60.dp, height = 30.dp),
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.CenterEnd),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "닫기",
                modifier = Modifier.size(44.dp),
                tint = LogitTheme.colors.primary600,
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
            .clickable(onClick = onClick)
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
            state = AccountScreen.State(
                userProfile = UserProfile("로짓", ""),
                reportNotificationEnabled = true,
                showLogoutDialog = false,
                showWithdrawDialog = false,
                showGuideWebView = false,
                showProfileWebView = false,
                tokenBalance = 3800,
                tokenLimit = 5000,
                isTokenBalanceLoaded = true,
            )
        )
    }
}

@Preview(name = "Account WebView", showBackground = true)
@Composable
private fun AccountWebViewPreview() {
    LogitTheme {
        AccountWebView(
            url = PROFILE_URL,
            onDismiss = {},
        )
    }
}

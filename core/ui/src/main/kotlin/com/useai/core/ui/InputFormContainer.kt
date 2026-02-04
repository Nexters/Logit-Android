package com.useai.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.appbar.PopUpTitle
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun InputFormContainer(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    bottomButtonText: String,
    onClickBottomButton: () -> Unit,
    bottomButtonEnabled: Boolean,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = LogitTheme.colors.white,
        // 내비바와 키보드 padding이 중복 계산되어 여백이 생기는 문제 방지
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.union(WindowInsets.ime),
        topBar = {
            PopUpTitle(
                onClick = onClickBackButton
            )
        },
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
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = dimensionResource(R.dimen.spacing_form_horizontal),
                        vertical = dimensionResource(R.dimen.spacing_form_vertical)
                    ),
            ) {
                content()
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
                    text = bottomButtonText,
                    onClick = onClickBottomButton,
                    enabled = bottomButtonEnabled,
                )
            }
        }
    }
}

package com.useai.feature.experience.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme

@Composable
internal fun ExperienceEmptyUI(
    onClickRegister: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 260.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_logit_empty),
                tint = Color.Unspecified,
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.experience_empty),
                style = LogitTheme.typography.body5_5,
                color = LogitTheme.colors.gray100,
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            LogitPrimaryButton(
                text = stringResource(R.string.experience_register),
                textStyle = LogitTheme.typography.body5_2,
                onClick = onClickRegister,
                modifier = Modifier.width(170.dp)
            )
        }
    }
}

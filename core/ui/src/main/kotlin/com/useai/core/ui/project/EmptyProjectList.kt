package com.useai.core.ui.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.component.button.LogitPrimaryButton
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun EmptyProjectList(
    modifier: Modifier = Modifier,
    onClickCreateProject: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 42.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_empty_state),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.home_empty_project_phrase),
                style = LogitTheme.typography.body6_2,
                color = LogitTheme.colors.gray100,
            )
            Spacer(Modifier.height(17.dp))
            LogitPrimaryButton(
                text = stringResource(R.string.home_new_project),
                onClick = {
                    onClickCreateProject()
                },
                textStyle = LogitTheme.typography.body6_2,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 7.dp),
            )
        }
    }
}

@Preview
@Composable
private fun EmptyProjectListPreview() {
    LogitTheme {
        EmptyProjectList(
            modifier = Modifier.background(LogitTheme.colors.white),
            onClickCreateProject = {},
        )
    }
}

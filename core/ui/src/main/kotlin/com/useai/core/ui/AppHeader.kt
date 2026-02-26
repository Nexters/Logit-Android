package com.useai.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.useai.core.designsystem.R

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    iconModel: Any?,
    iconDescription: String?,
    iconSize: Dp,
    onIconClick: () -> Unit,
    placeholder: Painter? = null,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = dimensionResource(R.dimen.screen_common_padding_horizontal),
        vertical = 12.dp,
    )
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        title()
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier.size(dimensionResource(R.dimen.app_header_user_profile_area_size)),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = iconModel,
                contentDescription = iconDescription,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .clickable(
                        onClick = onIconClick,
                    ),
                placeholder = placeholder,
                error = placeholder,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
private fun AppHeaderPreview() {
    AppHeader(
        title = {
            Image(
                painter = painterResource(R.drawable.ic_symbol_word),
                contentDescription = stringResource(R.string.content_description_app_logo),
                modifier = Modifier
                    .height(28.dp)
                    .width(85.dp),
            )
        },
        iconModel = R.drawable.ic_app_user,
        iconDescription = stringResource(R.string.content_description_user_profile),
        iconSize = dimensionResource(R.dimen.app_header_user_profile_image_size),
        onIconClick = {}
    )
}

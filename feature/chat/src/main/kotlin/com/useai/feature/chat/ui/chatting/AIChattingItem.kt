package com.useai.feature.chat.ui.chatting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import com.useai.core.model.chat.ChattingContent
import java.time.LocalDateTime

@Composable
internal fun AIChattingItem(
    chatting: ChattingContent.AI,
    onUpdateLetterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isStreaming: Boolean = false,
    fadeFromGradient: Boolean = false
) {
    var showFade by remember(chatting.id, fadeFromGradient) { mutableStateOf(fadeFromGradient) }
    LaunchedEffect(fadeFromGradient, chatting.id) {
        if (fadeFromGradient) showFade = true
    }
    val fadeProgress by animateFloatAsState(
        targetValue = if (showFade) 1f else 0f,
        animationSpec = tween(durationMillis = 650, easing = LinearEasing),
        finishedListener = { if (showFade) showFade = false },
        label = "streamingGradientFade"
    )

    val finalColorProgress = if (showFade) fadeProgress else 1f
    val finalTextColor = lerp(
        start = Color(0xFF6E7CD0),
        stop = LogitTheme.colors.black,
        fraction = finalColorProgress
    )
    val finalTextStyle = LogitTheme.typography.body7_3.copy(color = finalTextColor)
    val streamingBaseTextStyle = LogitTheme.typography.body7_3.copy(
        color = Color(0xFF6E7CD0).copy(alpha = 0.92f)
    )

    val (gradientStyle, streamingSweepAlpha) = rememberStreamingGradientSpec()
    val shouldRenderGradientOverlay = isStreaming || showFade
    val shouldRenderFinalText = true

    val gradientAlpha = if (isStreaming) {
        streamingSweepAlpha
    } else {
        1f - fadeProgress
    }

    Column(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_ai_word),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )

        Box(modifier = Modifier.padding(top = 8.dp)) {
            if (shouldRenderFinalText) {
                Text(
                    text = chatting.message,
                    style = if (isStreaming) streamingBaseTextStyle else finalTextStyle
                )
            }
            if (shouldRenderGradientOverlay) {
                Text(
                    text = chatting.message,
                    style = gradientStyle,
                    modifier = Modifier.graphicsLayer(alpha = gradientAlpha)
                )
            }
        }

        if (chatting.isLetter) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onUpdateLetterClick(chatting.message) }
                    .padding(vertical = 12.dp, horizontal = 3.dp)
                    .semantics(mergeDescendants = true) {},
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_autorenew),
                    contentDescription = null,
                    tint = LogitTheme.colors.primary400
                )
                Text(
                    text = stringResource(R.string.update_letter),
                    modifier = Modifier.padding(start = 6.dp),
                    style = LogitTheme.typography.body8_1,
                    color = LogitTheme.colors.primary400
                )
            }
        }
    }
}

@Composable
private fun rememberStreamingGradientSpec(): Pair<TextStyle, Float> {
    val transition = rememberInfiniteTransition(label = "streamingGradient")
    val vectorDx = 260f
    val vectorDy = 260f
    val sweepDurationMillis = 1200
    val pauseDurationMillis = 900
    val cycleDurationMillis = sweepDurationMillis + pauseDurationMillis

    val sweepProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = cycleDurationMillis
                0f at 0
                1f at sweepDurationMillis
                1f at cycleDurationMillis
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "streamingGradientSweep"
    )
    val overlayAlpha = when {
        sweepProgress <= 0f -> 0f
        sweepProgress < 0.08f -> sweepProgress / 0.08f
        sweepProgress < 0.92f -> 1f
        sweepProgress < 1f -> (1f - sweepProgress) / 0.08f
        else -> 0f
    }

    val start = Offset(
        x = -240f + (760f * sweepProgress),
        y = -220f + (520f * sweepProgress)
    )
    val end = Offset(start.x + vectorDx, start.y + vectorDy)

    val style = LogitTheme.typography.body7_3.copy(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFF6E7CD0).copy(alpha = 0.15f),
                Color(0xFF43B3C7).copy(alpha = 0.9f),
                Color(0xFF6E7CD0).copy(alpha = 0.25f),
                Color.Transparent
            ),
            start = start,
            end = end,
            tileMode = TileMode.Clamp
        )
    )
    return style to overlayAlpha
}

@Preview(showBackground = true)
@Composable
private fun AIChattingItemPreview(){
    AIChattingItem(
        chatting = ChattingContent.AI(
            message = "나랏말싸미 듕귁에 달아 문자와로 " +
                    "서르 사맛디 아니할쎄 이런 전차로 어린" +
                    " 백셩이 니르고져 홀 배 이셔도 마참내 제 뜨들" +
                    " 시러 펴디 몯핧 노미 하니라 내 이랄 위핧야 " +
                    "어엿비 너겨 새로 스믈여딻 자랄 맹가노니" +
                    " 사람마다 히여 수비 니겨 날로 쓰메 편안킈" +
                    " 하고져 핧 따라미니라",
            isLetter = true,
            id = "",
            createdAt = LocalDateTime.MIN
        ),
        onUpdateLetterClick = {},
        modifier = Modifier,
        isStreaming = false,
        fadeFromGradient = false
    )
}

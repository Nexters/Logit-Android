package com.useai.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme

@Composable
fun LogitPageLoadingView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LogitTheme.colors.white),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingRing(modifier = Modifier.size(64.dp))
        Text(
            text = stringResource(R.string.common_loading),
            style = TextStyle(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF58AEF4), Color(0xFF65C1ED))
                ),
                fontSize = 16.sp,
                lineHeight = 19.2.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun LoadingRing(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_ring_rotation")
    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading_ring_rotation_value"
    )
    Canvas(modifier = modifier.rotate(rotation.value)) {
        val stroke = Stroke(width = 32f, cap = StrokeCap.Butt)
        drawArc(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFBFD1FF), Color(0xFF94D7E2)),
                start = Offset(0f, size.height),
                end = Offset(size.width, 0f)
            ),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke
        )
    }
}

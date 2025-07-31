package com.qriz.app.core.ui.common.const

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color,
    interval: FloatArray,
    thickness: Float = 1f,
    width: Float = 1f,
) {
    Canvas(
        modifier = modifier
            .height(thickness.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(
            intervals = interval,
            phase = 0f
        )

        drawLine(
            color = color,
            start = Offset(
                0f,
                size.height / 2
            ),
            end = Offset(
                size.width,
                size.height / 2
            ),
            strokeWidth = width.dp.toPx(),
            pathEffect = pathEffect,
        )
    }
}

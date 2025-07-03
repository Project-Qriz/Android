package com.qriz.app.feature.daily_study.status.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

// TODO: 리팩토링 필요! 
@Composable
fun TestCompleteTooltip(
    modifier: Modifier = Modifier,
    text: String,
    borderRadius: Dp = 6.dp,
    color: Color = Color(0xFF3F444C)
) {
    Box(modifier = modifier
        .padding(top = 8.dp)
        .drawBehind {
            val tailWidth = 16.dp.toPx()
            val tailHeight = 8.dp.toPx()

            drawRoundRect(
                color = color,
                topLeft = Offset(
                    0f,
                    8.dp.toPx()
                ),
                size = Size(
                    size.width,
                    size.height - 8.dp.toPx()
                ),
                cornerRadius = CornerRadius(borderRadius.toPx())
            )

            val tailPath = Path().apply {
                moveTo(
                    size.width / 2 - tailWidth / 2,
                    tailHeight
                )
                lineTo(
                    size.width / 2,
                    0f
                )
                lineTo(
                    size.width / 2 + tailWidth / 2,
                    tailHeight
                )
                close()
            }
            drawPath(
                tailPath,
                color
            )
        }) {
        Text(
            text = text,
            color = White,
            style = QrizTheme.typography.label2,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestCompleteTooltipPreview() {
    QrizTheme {
        TestCompleteTooltip(text = "학습 완료된 테스트를 누르면 결과 화면으로 넘어갑니다.")
    }
}

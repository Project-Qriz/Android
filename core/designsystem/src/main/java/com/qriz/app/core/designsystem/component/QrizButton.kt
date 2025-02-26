package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@Composable
fun QrizButton(
    text: String,
    enable: Boolean = true,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier.padding(
        vertical = 12.dp,
        horizontal = 5.dp
    ),
    onClick: () -> Unit,
    cornerRadiusDp : Dp = 8.dp,
    containerColor: Color? = null,
    strokeColor: Color? = null,
    textColor: Color? = null,
) {
    val backgroundColor = if (enable) containerColor ?: Blue500 else Gray200
    val selectedTextColor = if (enable) textColor ?: White else Gray500
    val borderColor = strokeColor ?: Color.Transparent

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadiusDp)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadiusDp)
            )
            .clickable(
                enabled = enable,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = QrizTheme.typography.headline2,
            color = selectedTextColor,
            modifier = textModifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QrizButtonEnablePreview() {
    QrizTheme {
        QrizButton(
            enable = true,
            text = "확인",
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QrizButtonColorPreview() {
    QrizTheme {
        QrizButton(
            enable = true,
            text = "다음",
            onClick = {},
            containerColor = Gray700
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun QrizButtonStrokeColorPreview() {
    QrizTheme {
        QrizButton(
            enable = true,
            text = "취소",
            onClick = {},
            strokeColor = Blue500,
            textColor = Blue500,
            containerColor = White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QrizButtonDisablePreview() {
    QrizTheme {
        QrizButton(
            enable = false,
            text = "확인",
            onClick = {}
        )
    }
}

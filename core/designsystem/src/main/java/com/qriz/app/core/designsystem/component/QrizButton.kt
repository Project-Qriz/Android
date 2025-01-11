package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun QrizButton(
    text: String,
    enable: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color? = null,
) {
    val containerColor =
        if (enable) color ?: MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary

    val textColor =
        if (enable) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSecondary

    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                enabled = enable,
                onClick = onClick,
            )
            .padding(
                vertical = 12.dp,
                horizontal = 5.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = QrizTheme.typography.headline2.copy(color = textColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QrizButtonEnablePreview() {
    QrizTheme {
        QrizButton(
            enable = true,
            text = "Enabled",
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QrizButtonForcedColorPreview() {
    QrizTheme {
        QrizButton(
            enable = true,
            text = "Enabled",
            onClick = {},
            color = Gray700
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QrizButtonDisablePreview() {
    QrizTheme {
        QrizButton(
            enable = false,
            text = "Enabled",
            onClick = {}
        )
    }
}

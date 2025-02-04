package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.White

@Composable
fun QrizCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 3.dp,
    onClick: (() -> Unit)? = null,
    color: Color = White,
    border: BorderStroke? =
        if (elevation != 0.dp) null
        else BorderStroke(
            width = 1.dp,
            color = Gray200
        ),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        onClick = onClick ?: {},
        enabled = onClick != null,
        shadowElevation = elevation,
        color = color,
        shape = RoundedCornerShape(12.dp),
        border = border,
        content = content
    )
}

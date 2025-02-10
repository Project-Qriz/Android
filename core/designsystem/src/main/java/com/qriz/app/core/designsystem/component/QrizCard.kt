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
    cornerRadius: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    if (onClick != null) {
        Surface(
            modifier = modifier,
            onClick = onClick,
            shadowElevation = elevation,
            color = color,
            shape = RoundedCornerShape(cornerRadius),
            border = border,
            content = content
        )
    } else {
        Surface(
            modifier = modifier,
            shadowElevation = elevation,
            color = color,
            shape = RoundedCornerShape(cornerRadius),
            border = border,
            content = content
        )
    }

}

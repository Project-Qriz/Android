package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun QrizCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.clickable { onClick?.invoke() },
        shape = RoundedCornerShape(8.dp),
        shadowElevation = elevation,
        tonalElevation = 0.dp
    ) {
        content()
    }
}

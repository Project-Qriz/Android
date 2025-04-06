package com.qriz.app.core.designsystem.component.box

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun QrizBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable QrizBoxScope.() -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {
        QrizBoxScopeInstance.content()
    }
}

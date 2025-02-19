package com.qriz.app.core.ui.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.White

@Composable
fun TestResultBaseCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    title: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(backgroundColor)
            .padding(
                vertical = 24.dp,
                horizontal = 18.dp
            )
    ) {
        title()

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )

        content()
    }
}

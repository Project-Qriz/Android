package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun QrizLoading(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Box(
        modifier = modifier
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Blue500
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun QrizLoadingPreview() {
    QrizTheme {
        QrizLoading()
    }
}

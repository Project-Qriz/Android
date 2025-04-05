package com.qriz.app.core.ui.common.const

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.R

@Composable
fun ErrorScreen(
    title: String,
    description: String,
    onClickRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = QrizTheme.typography.headline3,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                style = QrizTheme.typography.subhead,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            QrizButton(
                text = stringResource(R.string.retry),
                cornerRadiusDp = 4.dp,
                textModifier = Modifier
                    .padding(
                        vertical = 9.dp,
                        horizontal = 41.dp
                    ),
                onClick = onClickRetry,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    QrizTheme {
        ErrorScreen (
            title = "오류가 발생했습니다.",
            description = "다시 시도해주세요.",
            onClickRetry = {}
        )
    }
}

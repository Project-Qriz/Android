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
fun NetworkErrorScreen(
    onClickRetry: () -> Unit
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
                text = stringResource(R.string.internet_connection_is_unstable),
                style = QrizTheme.typography.headline3,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.check_network_and_try_again),
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
private fun NetworkErrorScreenPreview() {
    QrizTheme {
        NetworkErrorScreen(
            onClickRetry = {}
        )
    }
}

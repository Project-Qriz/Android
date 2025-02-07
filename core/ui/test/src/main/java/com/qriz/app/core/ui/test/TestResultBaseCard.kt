package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun TestResultBaseCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subTitle: String,
    chartTitle: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    QrizCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            title()

            HorizontalDivider(
                color = Blue100,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        bottom = 16.dp
                    )
            )

            Text(
                text = subTitle,
                style = QrizTheme.typography.subhead,
                color = Gray600,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            chartTitle?.invoke()

            content()
        }
    }
}

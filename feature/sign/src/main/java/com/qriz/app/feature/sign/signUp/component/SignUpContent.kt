package com.qriz.app.feature.sign.signUp.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton

@Composable
internal fun SignUpContent(
    title: String,
    subTitle: String,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 24.dp,
                horizontal = 18.dp,
            )
            .imePadding()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W600
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = subTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        content()

        Spacer(modifier = Modifier.weight(1f))

        QrizButton(
            enable = buttonEnabled,
            text = buttonText,
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

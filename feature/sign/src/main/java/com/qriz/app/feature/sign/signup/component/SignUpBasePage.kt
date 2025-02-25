package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.layout.Column
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
internal fun SignUpBasePage(
    title: String,
    buttonText: String,
    buttonEnabled: Boolean,
    subTitle: String? = null,
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
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val titleBottomPadding = if (subTitle != null) 8.dp else 32.dp

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.W600
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = titleBottomPadding),
            )

            if (subTitle != null) {
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(bottom = 32.dp),
                )
            }

            content()
        }

        QrizButton(
            enable = buttonEnabled,
            text = buttonText,
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

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
            .background(White)
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
                style = QrizTheme.typography.heading1,
                color = Gray800,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            if (subTitle != null) {
                Text(
                    text = subTitle,
                    style = QrizTheme.typography.body1,
                    color = Gray500,
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

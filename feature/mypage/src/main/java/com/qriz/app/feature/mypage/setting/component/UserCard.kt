package com.qriz.app.feature.mypage.setting.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
internal fun UserCard(
    modifier: Modifier = Modifier,
    userName: String,
    email: String,
) {
    QrizCard(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = Blue100,
        ),
        elevation = 0.dp
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 21.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = userName,
                style = QrizTheme.typography.heading1.copy(color = Gray800)
            )

            Text(
                text = email,
                style = QrizTheme.typography.subhead.copy(color = Gray400)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserCardPreview() {
    QrizTheme {
        UserCard(
            modifier = Modifier.padding(16.dp),
            userName = "홍길동",
            email = "hong@example.com",
        )
    }
}

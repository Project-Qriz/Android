package com.qriz.app.feature.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
internal fun UserSection(
    modifier: Modifier = Modifier,
    userName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = userName,
            style = QrizTheme.typography.heading1.copy(color = Gray800)
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
            tint = Gray800,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserSectionPreview() {
    QrizTheme {
        UserSection(
            userName = "홍길동",
            onClick = {}
        )
    }
}

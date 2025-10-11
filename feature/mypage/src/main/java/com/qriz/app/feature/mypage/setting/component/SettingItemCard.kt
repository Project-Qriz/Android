package com.qriz.app.feature.mypage.setting.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
internal fun SettingItemCard(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    QrizCard(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = Blue100,
        ),
        elevation = 0.dp,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 21.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = QrizTheme.typography.subhead.copy(color = Gray800)
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
                contentDescription = null,
                tint = Gray800,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingItemCardPreview() {
    QrizTheme {
        SettingItemCard(
            modifier = Modifier.padding(16.dp),
            title = "개인정보 처리방침",
            onClick = {},
        )
    }
}

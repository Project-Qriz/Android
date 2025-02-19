package com.qriz.app.feature.sign.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.sign.R

@Composable
fun FormatStateRow(
    message: String,
    isFormatValid: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.check_icon),
            contentDescription = null,
            tint = if (isFormatValid) Mint800 else Gray500,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = message,
            style = QrizTheme.typography.label2,
            color = if (isFormatValid) Mint800 else Gray500,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

@Preview
@Composable
fun FormatStateRowPreview() {
    FormatStateRow(
        message = "대문자/소문자/숫자/특수문자 포함",
        isFormatValid = true
    )
}

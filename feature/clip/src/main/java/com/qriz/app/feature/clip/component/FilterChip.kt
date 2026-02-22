package com.qriz.app.feature.clip.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
internal fun FilterChip(
    value: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {

    val borderColor = if (enabled) {
        if (isSelected) {
            Blue500
        } else {
            Gray200
        }
    } else {
        Gray200.copy(alpha = 0.3f)
    }

    val textColor = if (enabled) {
        if (isSelected) {
            Blue500
        } else {
            Gray700
        }
    } else {
        Gray200.copy(alpha = 0.3f)
    }

    Text(
        text = value,
        modifier = Modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        style = QrizTheme.typography.label2.copy(color = textColor),
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterChipSelectedPreview() {
    FilterChip(
        value = "DML",
        isSelected = true,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterChipEnabledPreview() {
    FilterChip(
        value = "DML",
        isSelected = false,
        enabled = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterChipDisabledPreview() {
    FilterChip(
        value = "DML",
        isSelected = false,
        enabled = false,
        onClick = {}
    )
}

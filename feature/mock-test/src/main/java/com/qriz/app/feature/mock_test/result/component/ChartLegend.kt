package com.qriz.app.feature.mock_test.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mock_test.R

@Composable
fun ChartLegend(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LegendItem(
            title = stringResource(R.string.mock_test_result_subject_1),
            color = Gray500,
        )
        LegendItem(
            title = stringResource(R.string.mock_test_result_subject_2),
            color = Blue500,
        )
    }
}

@Composable
private fun LegendItem(
    title: String,
    color: Color,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )
        Text(
            text = title,
            style = QrizTheme.typography.body2.copy(color = Gray500)
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun ChartLegendPreview() {
    QrizTheme {
        ChartLegend()
    }
}

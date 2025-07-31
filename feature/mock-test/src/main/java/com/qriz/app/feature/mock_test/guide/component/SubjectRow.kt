package com.qriz.app.feature.mock_test.guide.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.const.DashedDivider

@Composable
fun SubjectRow(
    modifier: Modifier = Modifier,
    title: String,
    totalScore: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = QrizTheme.typography.body2.copy(color = Gray800)
        )
        DashedDivider(
            modifier = Modifier.padding(horizontal = 12.dp).weight(1f),
            color = Gray200,
            interval = floatArrayOf(10f, 10f),
        )
        Text(
            text = totalScore.toString(),
            style = QrizTheme.typography.body2.copy(color = Gray800)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubjectRowPreview() {
    QrizTheme {
        SubjectRow(
            title = "데이터 모델링의 이해",
            totalScore = 100
        )
    }
}

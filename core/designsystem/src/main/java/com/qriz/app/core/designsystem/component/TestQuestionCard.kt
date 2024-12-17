package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun TestQuestionCard(
    question: String,
    modifier: Modifier = Modifier,
) {
    QrizCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(24.dp),
        )
    }
}

@Preview
@Composable
fun TestQuestionCardPreview() {
    QrizTheme {
        TestQuestionCard("다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?")
    }
}


package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun TestPage(
    question: String,
    options: List<String>,
    selected: List<String>,
    modifier: Modifier = Modifier,
    description: String? = null,
    onSelected: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        if (description != null) {
            DescriptionCard(
                description = description,
                modifier = Modifier.padding(bottom = 10.dp),
            )
        }

        options.forEachIndexed { index, option ->
            val state = if (selected.contains(option)) TestOptionState.SelectedOrCorrect
            else TestOptionState.None
            TestOptionCard(
                state = state,
                number = index,
                content = option,
                modifier = Modifier.padding(bottom = 8.dp)
                    .clickable { onSelected(option) }
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun TestPagePreview() {
    QrizTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            TestPage(
                question = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?",
                options = listOf(
                    "특정 열을 기준으로 행을 그룹화한다",
                    "집계 함수와 함께 자주 사용된다",
                    "WHERE 절 다음에 위치한다",
                    "ORDER BY 절 다음에 위치한다",
                ),
                selected = listOf(
                    "집계 함수와 함께 자주 사용된다",
                ),
                onSelected = {}
            )
        }
    }
}

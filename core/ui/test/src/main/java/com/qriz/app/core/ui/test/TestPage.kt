package com.qriz.app.core.ui.test

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
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.designsystem.theme.QrizTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TestPage(
    modifier: Modifier = Modifier,
    question: String,
    options: ImmutableList<Option>,
    isResultPage: Boolean,
    selectedOption: Option?,
    correctOption: Option? = null,
    description: String? = null,
    onSelected: ((Option) -> Unit)? = null,
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
            TestDescriptionCard(
                description = description,
                modifier = Modifier.padding(bottom = 10.dp),
            )
        }

        options.forEachIndexed { index, option ->
            val isSelected = option == selectedOption

            val state = if (isResultPage) {
                val isCorrect = option == correctOption
                val isWrongChoice = isSelected.not() && isCorrect
                val isGoodChoice = isSelected && isCorrect
                when {
                    isWrongChoice -> TestOptionState.SelectedAndIncorrect
                    isGoodChoice || isSelected -> TestOptionState.SelectedOrCorrect
                    else -> TestOptionState.None
                }
            } else {
                when {
                    isSelected -> TestOptionState.SelectedOrCorrect
                    else -> TestOptionState.None
                }
            }
            TestOptionCard(
                state = state,
                number = index + 1,
                option = option,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable(
                        enabled = isResultPage.not(),
                        onClick = { onSelected?.let { onSelected(option) } }
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestPagePreview() {
    QrizTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            TestPage(
                question = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?",
                options = persistentListOf(
                    Option("특정 열을 기준으로 행을 그룹화한다"),
                    Option("집계 함수와 함께 자주 사용된다"),
                    Option("WHERE 절 다음에 위치한다"),
                    Option("ORDER BY 절 다음에 위치한다"),
                ),
                selectedOption = Option("집계 함수와 함께 자주 사용된다"),
                isResultPage = false,
                description = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?asfasf",
                onSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestResultPagePreview() {
    QrizTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            TestPage(
                question = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?",
                options = persistentListOf(
                    Option("특정 열을 기준으로 행을 그룹화한다"),
                    Option("집계 함수와 함께 자주 사용된다"),
                    Option("WHERE 절 다음에 위치한다"),
                    Option("ORDER BY 절 다음에 위치한다"),
                ),
                selectedOption = Option("집계 함수와 함께 자주 사용된다"),
                correctOption = Option("WHERE 절 다음에 위치한다"),
                isResultPage = true,
                description = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?asafasfasf",
                onSelected = {}
            )
        }
    }
}

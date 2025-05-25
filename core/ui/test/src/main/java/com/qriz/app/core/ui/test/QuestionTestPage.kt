package com.qriz.app.core.ui.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.minutes

@Composable
fun QuestionTestPage(
    modifier: Modifier = Modifier,
    question: QuestionTestItem,
    questionNum: Int,
    onSelected: ((OptionItem) -> Unit)? = null,
) {
    QuestionBasePage(
        modifier = modifier.fillMaxWidth(),
        questionText = question.question,
        description = question.description,
        questionNum = questionNum,
    ) {
        question.options.forEachIndexed { index, option ->
            val state = when (option) {
                is SelectedOrCorrectOptionItem -> TestOptionState.SelectedOrCorrect
                else -> TestOptionState.None
            }
            QuestionOptionCard(
                state = state,
                number = index + 1,
                optionDescription = option.description,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable { onSelected?.let { onSelected(option) } }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionPagePreview() {
    QrizTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            QuestionTestPage(
                question = QuestionTestItem(
                    id = 1L,
                    question = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?",
                    options = persistentListOf(
                        GeneralOptionItem(0, "특정 열을 기준으로 행을 그룹화한다"),
                        SelectedOrCorrectOptionItem(0, "집계 함수와 함께 자주 사용된다"),
                        GeneralOptionItem(0, "WHERE 절 다음에 위치한다"),
                        GeneralOptionItem(0, "ORDER BY 절 다음에 위치한다"),
                    ),
                    description = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?asfasf",
                    timeLimit = 1.minutes.inWholeMilliseconds.toInt(),
                    isOptionSelected = true
                ),
                onSelected = {},
                questionNum = 1
            )
        }
    }
}

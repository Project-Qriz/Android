package com.qriz.app.core.ui.test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.QuestionResultItem
import com.qriz.app.core.ui.test.model.SelectedAndIncorrectOptionItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import kotlinx.collections.immutable.persistentListOf

@Composable
fun QuestionResultPage(
    modifier: Modifier = Modifier,
    questionResult: QuestionResultItem,
    questionNum: Int,
) {
    QuestionBasePage(
        modifier = modifier.fillMaxWidth(),
        questionText = questionResult.question,
        questionNum = questionNum,
    ) {
        questionResult.options.forEachIndexed { index, option ->
            val state = when (option) {
                is GeneralOptionItem -> TestOptionState.None
                is SelectedAndIncorrectOptionItem -> TestOptionState.SelectedAndIncorrect
                is SelectedOrCorrectOptionItem -> TestOptionState.SelectedOrCorrect
            }
            QuestionOptionCard(
                state = state,
                number = index + 1,
                optionDescription = option.description,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionResultPagePreview() {
    QrizTheme {
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            QuestionResultPage(
                questionResult = QuestionResultItem(
                    skillName = "asdasd",
                    question = "다음 중 GROUP BY 절의 특징으로 올바르지 않은 것은?",
                    options = persistentListOf(
                        GeneralOptionItem(0,"특정 열을 기준으로 행을 그룹화한다"),
                        SelectedAndIncorrectOptionItem(0, "집계 함수와 함께 자주 사용된다"),
                        SelectedOrCorrectOptionItem(0, "WHERE 절 다음에 위치한다"),
                        GeneralOptionItem(0, "ORDER BY 절 다음에 위치한다"),
                    ),
                    solution = "asd",
                    correction = false
                ),
                questionNum = 1
            )
        }
    }
}

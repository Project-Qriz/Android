package com.qriz.app.core.ui.test

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.minutes

@Composable
fun TestScreen(
    questions: ImmutableList<QuestionTestItem>,
    testTimeType: TestTimeType,
    currentIndex: Int,
    remainTimeText: String,
    progressPercent: Float,
    isLoading: Boolean,
    onSelectOption: (Long, OptionItem) -> Unit,
    onClickNextPage: () -> Unit,
    onClickSubmit: () -> Unit,
    onClickPreviousPage: () -> Unit,
    onClickCancel: () -> Unit,
) {
    val pagerState = rememberPagerState { questions.size }

    LaunchedEffect(currentIndex) {
        pagerState.animateScrollToPage(currentIndex)
    }

    BackHandler {
        onClickPreviousPage()
    }

    Column {
        TestTopBar(
            remainTimeText = remainTimeText,
            progressPercent = progressPercent,
            testTimeType = testTimeType,
            onCancel = onClickCancel,
        )

        Box(
            modifier = Modifier.weight(1f),
        ) {
            if (isLoading) QrizLoading()

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false,
                verticalAlignment = Alignment.Top,
            ) { index ->
                val question = questions[index]
                val scrollState = rememberScrollState()
                QuestionTestPage(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(
                            vertical = 24.dp,
                            horizontal = 18.dp
                        ),
                    question = question,
                    questionNum = index + 1,
                    onSelected = { selected ->
                        onSelectOption(
                            question.id,
                            selected
                        )
                    },
                )
            }
        }

        TestPageBottomNavigator(
            currentIndex = currentIndex,
            lastIndex = questions.lastIndex,
            canTurnNextPage = questions[currentIndex].isOptionSelected,
            onClickNextPage = onClickNextPage,
            onClickSubmit = onClickSubmit,
            onClickPreviousPage = onClickPreviousPage
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TestScreenPreview() {
    val testData = persistentListOf(
        QuestionTestItem(
            id = 1,
            question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
            options = persistentListOf(
                GeneralOptionItem("트랜잭션을 더 작은 단위로 분할"),
                SelectedOrCorrectOptionItem("트랜잭션의 타임아웃 시간을 늘림"),
                GeneralOptionItem("모든 데이터를 메모리에 로드"),
                GeneralOptionItem("트랜잭션의 격리 수준을 낮춤"),
            ),
            timeLimit = 1.minutes.inWholeMilliseconds.toInt(),
            isOptionSelected = true,
        ),
        QuestionTestItem(
            id = 2,
            question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
            options = persistentListOf(
                SelectedOrCorrectOptionItem("트랜잭션을 더 작은 단위로 분할#2"),
                GeneralOptionItem("트랜잭션의 타임아웃 시간을 늘림#2"),
                GeneralOptionItem("모든 데이터를 메모리에 로드#2"),
                GeneralOptionItem("트랜잭션의 격리 수준을 낮춤#2"),
            ),
            timeLimit = 1.minutes.inWholeMilliseconds.toInt(),
            isOptionSelected = true,
        )
    )
    QrizTheme {
        TestScreen(
            questions = testData,
            testTimeType = TestTimeType.TOTAL,
            currentIndex = 0,
            remainTimeText = "25:00",
            progressPercent = 0.5f,
            isLoading = true,
            onSelectOption = { _, _ -> },
            onClickNextPage = {},
            onClickPreviousPage = {},
            onClickCancel = {},
            onClickSubmit = {},
        )
    }
}


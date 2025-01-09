package com.qriz.app.core.ui.test

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
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.theme.QrizTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TestScreen(
    questions: ImmutableList<Question>,
    selectedOptions: ImmutableMap<Long, Option>,
    testTimeType: TestTimeType,
    currentIndex: Int,
    remainTimeText: String,
    progressPercent: Float,
    canTurnNextPage: Boolean,
    isLoading: Boolean,
    onSelectOption: (Long, Option) -> Unit,
    onClickNextPage: () -> Unit,
    onClickSubmit: () -> Unit,
    onClickPreviousPage: () -> Unit,
    onClickCancel: () -> Unit,
) {
    val pagerState = rememberPagerState { questions.size }

    LaunchedEffect(currentIndex) {
        pagerState.animateScrollToPage(currentIndex)
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
                TestPage(
                    question = question.question,
                    options = question.options.toImmutableList(),
                    selectedOption = selectedOptions[question.id],
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(
                            vertical = 24.dp,
                            horizontal = 18.dp
                        ),
                    onSelected = { selected ->
                        onSelectOption(
                            question.id,
                            selected
                        )
                    },
                    isResultPage = false,
                )
            }
        }


        TestPageBottomNavigator(
            currentIndex = currentIndex,
            lastIndex = questions.lastIndex,
            canTurnNextPage = canTurnNextPage,
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
        Question(
            id = 1,
            question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
            options = persistentListOf(
                Option("트랜잭션을 더 작은 단위로 분할"),
                Option("트랜잭션의 타임아웃 시간을 늘림"),
                Option("모든 데이터를 메모리에 로드"),
                Option("트랜잭션의 격리 수준을 낮춤"),
            ),
            timeLimit = 60,
        ),
        Question(
            id = 2,
            question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
            options = persistentListOf(
                Option("트랜잭션을 더 작은 단위로 분할#2"),
                Option("트랜잭션의 타임아웃 시간을 늘림#2"),
                Option("모든 데이터를 메모리에 로드#2"),
                Option("트랜잭션의 격리 수준을 낮춤#2"),
            ),
            timeLimit = 60,
        )
    )
    QrizTheme {
        TestScreen(
            questions = testData,
            selectedOptions = persistentMapOf(
                1L to Option("트랜잭션의 격리 수준을 낮춤")
            ),
            testTimeType = TestTimeType.TOTAL,
            currentIndex = 0,
            remainTimeText = "25:00",
            progressPercent = 0.5f,
            canTurnNextPage = true,
            isLoading = true,
            onSelectOption = { _, _ -> },
            onClickNextPage = {},
            onClickPreviousPage = {},
            onClickCancel = {},
            onClickSubmit = {},
        )
    }
}


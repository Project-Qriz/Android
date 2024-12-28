package com.qriz.app.feature.onboard.ui.screen.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.data.onboard.onboard_api.model.Question
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.component.TestPage
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.onboard.model.PreviewEffect

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSubmit: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PreviewEffect.Submit -> onSubmit()
            }
        }
    }

    PreviewContent(
        questions = state.questions,
        myAnswer = state.myAnswer,
        progressPercent = state.progressPercent,
        remainTimeText = state.remainTimeText,
        currentPage = state.currentPage,
        canTurnNextPage = state.canTurnNextPage,
        onAnswerSelect = viewModel::saveAnswer,
        onNextPage = viewModel::nextPage,
        onPreviousPage = viewModel::previousPage,
        onBack = onBack,
    )
}

@Composable
private fun PreviewContent(
    questions: List<Question>,
    myAnswer: Map<Long, String>,
    progressPercent: Float,
    remainTimeText: String,
    currentPage: Int,
    canTurnNextPage: Boolean,
    onAnswerSelect: (Long, String) -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState { questions.size }

    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    Column {
        QrizTopBar(
            title = "",
            navigationType = NavigationType.Back,
            onNavigationClick = onBack,
            background = MaterialTheme.colorScheme.background,
            actions = {
                Text(
                    text = buildAnnotatedString {
                        append("전체 남은 시간  ")
                        withStyle(
                            SpanStyle(color = MaterialTheme.colorScheme.primary)
                        ) {
                            append(remainTimeText)
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 15.dp)
                )
            },
        )
        LinearProgressIndicator(
            progress = { progressPercent },
            trackColor = Gray200,
            color = Blue600,
            strokeCap = StrokeCap.Round,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page ->
            val question = questions[page]
            TestPage(
                question = question.question,
                options = question.options,
                selected = myAnswer[question.id]?.let { listOf(it) } ?: emptyList(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = 24.dp,
                        horizontal = 18.dp
                    ),
                onSelected = { selected ->
                    onAnswerSelect(
                        question.id,
                        selected
                    )
                },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 8.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QrizButton(
                enable = currentPage > 0,
                text = "이전",
                modifier = Modifier.width(90.dp),
                onClick = onPreviousPage,
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(fontWeight = FontWeight.W600)
                    ) {
                        append(currentPage.toString())
                    }
                    append(" / ${questions.size}")

                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            QrizButton(
                enable = canTurnNextPage,
                text = "다음",
                modifier = Modifier.width(90.dp),
                onClick = onNextPage,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewContentPreview() {
    QrizTheme {
        PreviewContent(
            questions = listOf(
                Question(
                    id = 1,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
                    options = listOf(
                        "트랜잭션을 더 작은 단위로 분할",
                        "트랜잭션의 타임아웃 시간을 늘림",
                        "모든 데이터를 메모리에 로드",
                        "트랜잭션의 격리 수준을 낮춤",
                    ),
                    timeLimit = 60,
                ),
                Question(
                    id = 2,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
                    options = listOf(
                        "트랜잭션을 더 작은 단위로 분할#2",
                        "트랜잭션의 타임아웃 시간을 늘림#2",
                        "모든 데이터를 메모리에 로드#2",
                        "트랜잭션의 격리 수준을 낮춤#2",
                    ),
                    timeLimit = 60,
                )
            ),
            myAnswer = mapOf(
                1L to "트랜잭션의 격리 수준을 낮춤"
            ),
            currentPage = 0,
            progressPercent = 0.5f,
            remainTimeText = "25:00",
            canTurnNextPage = true,
            onAnswerSelect = { _, _ -> },
            onNextPage = {},
            onPreviousPage = {},
            onBack = {},
        )
    }
}
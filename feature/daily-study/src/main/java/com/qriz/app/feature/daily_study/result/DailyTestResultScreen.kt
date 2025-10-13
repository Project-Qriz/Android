package com.qriz.app.feature.daily_study.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue300
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Blue800
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.daily_study.result.DailyTestResultUiState.ViewType
import com.qriz.app.feature.daily_study.result.component.DailyTestTotalScorePage
import com.qriz.app.feature.daily_study.result.component.WeeklyReviewResultDetailPage
import com.qriz.app.feature.daily_study.result.model.DailyTestQuestionResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.qriz.app.core.ui.common.R as UCR

private val TEST_RESULT_COLORS = listOf(
    Blue800,
    Blue500,
    Blue300,
    Blue100,
    Gray300
)

private fun getTestResultColor(index: Int): Color =
    if (index > TEST_RESULT_COLORS.lastIndex) TEST_RESULT_COLORS.last()
    else TEST_RESULT_COLORS[index]

@Composable
fun DailyTestResultScreen(
    viewModel: DailyTestResultViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            DailyTestResultUiEffect.Close -> onClose()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(DailyTestResultUiAction.LoadData)
    }

    when (val loadState = uiState.state) {
        is DailyTestResultUiState.LoadState.Loading -> QrizLoading()

        is DailyTestResultUiState.LoadState.Failure -> ErrorScreen(
            title = stringResource(UCR.string.error_occurs),
            description = loadState.message,
            onClickRetry = { viewModel.process(DailyTestResultUiAction.LoadData) },
        )

        is DailyTestResultUiState.LoadState.Success -> {
            val data = loadState.data

            DailyTestResultContent(
                day = uiState.day,
                userName = uiState.userName,
                viewType = uiState.viewType,
                selectedSubjectFilter = uiState.selectedSubjectFilter,
                showFilterDropDown = uiState.showFilterDropDown,
                passed = data.passed,
                totalScore = data.totalScore,
                testResultItems = data.skillScores,
                questionResultItems = data.questionResults,
                weeklyReviewState = uiState.weeklyReviewState,
                onClickFilter = { viewModel.process(DailyTestResultUiAction.ClickFilter) },
                onBackButtonClick = { viewModel.process(DailyTestResultUiAction.ClickBackButton) },
                onClickDetail = if (uiState.isReview) { { viewModel.process(DailyTestResultUiAction.ShowDetail) } }
                else null,
            )
        }
    }
}

@Composable
private fun DailyTestResultContent(
    day: Int,
    userName: String,
    passed: Boolean,
    totalScore: Int,
    weeklyReviewState: DailyTestResultUiState.WeeklyReviewState,
    showFilterDropDown: Boolean,
    viewType: ViewType,
    selectedSubjectFilter: ScoreDetailSubjectFilter,
    testResultItems: ImmutableList<TestResultItem>,
    questionResultItems: ImmutableList<DailyTestQuestionResultItem>,
    onClickFilter: () -> Unit,
    onBackButtonClick: () -> Unit,
    onClickDetail: (() -> Unit)? = null,
) {
    BackHandler { onBackButtonClick() }

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        QrizTopBar(
            title = if (viewType == ViewType.TOTAL) stringResource(R.string.test_result) else null,
            navigationType = if (viewType == ViewType.DETAIL) NavigationType.BACK else NavigationType.CLOSE,
            onNavigationClick = onBackButtonClick,
        )

        when(viewType) {
            ViewType.TOTAL -> DailyTestTotalScorePage(
                modifier = Modifier.weight(1f),
                day = day,
                userName = userName,
                passed = passed,
                totalScore = totalScore,
                testResultItems = testResultItems,
                questionResultItems = questionResultItems,
                getTestResultColor = ::getTestResultColor,
                onClickDetail = onClickDetail,
            )

            ViewType.DETAIL -> WeeklyReviewResultDetailPage(
                state = weeklyReviewState,
                showFilterDropDown = showFilterDropDown,
                selectedSubjectFilter = selectedSubjectFilter,
                onClickFilter = onClickFilter,
                getTestResultColor = ::getTestResultColor,
                onClickRetry = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyTestResultContentPreview() {
    DailyTestResultContent(
        day = 15,
        userName = "김철수",
        passed = false,
        totalScore = 85,
        selectedSubjectFilter = ScoreDetailSubjectFilter.TOTAL,
        showFilterDropDown = false,
        questionResultItems = persistentListOf(
            DailyTestQuestionResultItem(
                id = 1,
                question = "아래 테이블 T<S<R이 각각 다음과 같이 선언되었다. \n" + "다음 중 DELETE FROM T;를 수행한 후에 테이블 R에 남아있는 데이터로 가장 적절한 것은?",
                correct = true,
                tags = persistentListOf(
                    "엔터티",
                    "식별자"
                )
            ),
            DailyTestQuestionResultItem(
                id = 2,
                question = "아래 테이블 T<S<R이 각각 다음과 같이 선언되었다. \n" + "다음 중 DELETE FROM T;를 수행한 후에 테이블 R에 남아있는 데이터로 가장 적절한 것은?",
                correct = false,
                tags = persistentListOf(
                    "엔터티",
                    "식별자"
                )
            )
        ),
        testResultItems = persistentListOf(
            TestResultItem(
                scoreName = "데이터 모델링의 이해",
                score = 90
            ),
            TestResultItem(
                scoreName = "데이터 모델과 SQL",
                score = 80
            ),
            TestResultItem(
                scoreName = "SQL 기본",
                score = 88
            ),
            TestResultItem(
                scoreName = "SQL 활용",
                score = 82
            ),
            TestResultItem(
                scoreName = "관리 구문",
                score = 75
            )
        ),
        onClickDetail = {},
        weeklyReviewState = DailyTestResultUiState.WeeklyReviewState.Loading,
        viewType = ViewType.TOTAL,
        onClickFilter = {},
        onBackButtonClick = {},
    )
}

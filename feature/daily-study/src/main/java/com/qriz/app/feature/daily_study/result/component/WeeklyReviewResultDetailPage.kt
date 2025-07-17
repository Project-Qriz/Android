package com.qriz.app.feature.daily_study.result.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.TestResultDetailPage
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.common.R as UCR
import com.qriz.app.feature.daily_study.result.DailyTestResultUiState.WeeklyReviewState

@Composable
internal fun WeeklyReviewResultDetailPage(
    state: WeeklyReviewState,
    selectedSubjectFilter: ScoreDetailSubjectFilter,
    showFilterDropDown: Boolean,
    getTestResultColor: (Int) -> Color,
    onClickFilter: () -> Unit,
    onClickRetry: () -> Unit,
) {
    when (state) {
        is WeeklyReviewState.Success -> TestResultDetailPage(
            selectedSubject = selectedSubjectFilter,
            showFilterDropDown = showFilterDropDown,
            enableFilter = false,
            totalScore = state.data.totalScore,
            testResultItem = state.testResultItems,
            resultDetailItems = state.resultDetailItems,
            getTestResultColor = getTestResultColor,
            onClickFilter = onClickFilter,
        )

        is WeeklyReviewState.Failure -> ErrorScreen(
            title = stringResource(UCR.string.error_occurs),
            description = state.message,
            onClickRetry = onClickRetry,
        )

        is WeeklyReviewState.Loading -> QrizLoading()
    }
}

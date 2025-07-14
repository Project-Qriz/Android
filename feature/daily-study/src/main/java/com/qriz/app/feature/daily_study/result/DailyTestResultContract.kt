package com.qriz.app.feature.daily_study.result

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyReviewResult
import com.qriz.app.core.ui.test.model.ScoreDetailItem
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultDetailItem
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.daily_study.result.model.DailyTestResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class DailyTestResultUiState(
    val day: Int,
    val userName: String,
    val state: LoadState,
    val weeklyReviewState: WeeklyReviewState,
    val isReview: Boolean,
    val isComprehensiveReview: Boolean,
    val viewType: ViewType,
    val showFilterDropDown: Boolean,
    val selectedSubjectFilter: ScoreDetailSubjectFilter,
) : UiState {
    @Stable
    sealed interface LoadState {
        @Immutable
        data object Loading : LoadState

        @Immutable
        data class Success(val data: DailyTestResultItem) : LoadState

        @Immutable
        data class Failure(val message: String, val canRetry: Boolean) : LoadState
    }

    @Stable
    sealed interface WeeklyReviewState {
        @Immutable
        data object Loading : WeeklyReviewState

        @Immutable
        data class Success(val data: WeeklyReviewResult) : WeeklyReviewState {
            val testResultItems: ImmutableList<TestResultItem> =
                data.subjectItems.flatMap { it.categoryItems }.sortedByDescending { it.score }.map {
                    TestResultItem(
                        score = it.score,
                        scoreName = it.categoryName,
                    )
                }.toImmutableList()

            val resultDetailItems: ImmutableList<TestResultDetailItem> =
                data.subjectItems.flatMap { it.categoryItems }.map {
                    TestResultDetailItem(
                        name = it.categoryName,
                        score = it.score,
                        items = it.conceptItems.map {
                            ScoreDetailItem(
                                name = it.conceptName,
                                score = it.score,
                            )
                        }.toImmutableList()
                    )
                }.toImmutableList()
        }

        @Immutable
        data class Failure(val message: String) : WeeklyReviewState
    }

    enum class ViewType {
        TOTAL, DETAIL,
    }

    companion object {
        val DEFAULT = DailyTestResultUiState(
            day = 0,
            state = LoadState.Loading,
            weeklyReviewState = WeeklyReviewState.Loading,
            viewType = ViewType.DETAIL,
            userName = "",
            isReview = false,
            isComprehensiveReview = false,
            showFilterDropDown = false,
            selectedSubjectFilter = ScoreDetailSubjectFilter.TOTAL,
        )
    }
}

sealed interface DailyTestResultUiAction : UiAction {
    data object LoadData : DailyTestResultUiAction
    data object ClickFilter : DailyTestResultUiAction
    data object ShowDetail : DailyTestResultUiAction
    data object ClickBackButton : DailyTestResultUiAction
}

sealed interface DailyTestResultUiEffect : UiEffect {
    data object Close : DailyTestResultUiEffect
}

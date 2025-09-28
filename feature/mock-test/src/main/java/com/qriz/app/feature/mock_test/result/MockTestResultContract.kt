package com.qriz.app.feature.mock_test.result

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.mock_test.result.model.MockTestResultItem

data class MockTestResultUiState(
    val loadState: LoadState,
    val viewType: ViewType,
    val selectedSubjectFilter: ScoreDetailSubjectFilter,
    val showHistoryFilterDropdown: Boolean,
    val showDetailFilterDropdown: Boolean,
    val userName: String,
) : UiState {
    @Stable
    sealed interface LoadState {
        @Immutable
        data object Loading : LoadState

        @Immutable
        data class Success(val result: MockTestResultItem)  : LoadState

        @Immutable
        data class Failure(val message: String) : LoadState
    }

    enum class ViewType {
        TOTAL, DETAIL,
    }

    enum class HistoricalScoreFilter(val value: String) {
        TOTAL("전체"),
        SUBJECT("과목별"),
    }

    companion object {
        val DEFAULT = MockTestResultUiState(
            loadState = LoadState.Loading,
            viewType = ViewType.TOTAL,
            showHistoryFilterDropdown = false,
            showDetailFilterDropdown = false,
            selectedSubjectFilter = ScoreDetailSubjectFilter.TOTAL,
            userName = "",
        )
    }
}

sealed interface MockTestResultUiAction : UiAction {
    data object Load : MockTestResultUiAction
    data object OnClickClose : MockTestResultUiAction
    data object OnClickDetail : MockTestResultUiAction
    data object ShowHistoryFilterDropdown : MockTestResultUiAction
    data object DismissHistoryFilterDropdown : MockTestResultUiAction
    data object ShowDetailFilterDropdown : MockTestResultUiAction
    data object DismissDetailFilterDropdown : MockTestResultUiAction
    data object OnClickBackButton : MockTestResultUiAction
    data object OnClickHistoryFilter : MockTestResultUiAction
    data class OnChangeHistoryFilter(val filter: MockTestResultUiState.HistoricalScoreFilter) : MockTestResultUiAction
    data class OnSelectFilter(
        val filter: ScoreDetailSubjectFilter,
    ) : MockTestResultUiAction
}

sealed interface MockTestResultUiEffect : UiEffect {
    data object Close : MockTestResultUiEffect
}

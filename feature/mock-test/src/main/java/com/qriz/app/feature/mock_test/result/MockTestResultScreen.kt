package com.qriz.app.feature.mock_test.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mock_test.R
import com.qriz.app.feature.mock_test.result.component.MockTestDetailScorePage
import com.qriz.app.feature.mock_test.result.component.MockTestTotalScorePage
import com.qriz.app.feature.mock_test.result.model.MockTestResultItem
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MockTestResultScreen(
    viewModel: MockTestResultViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is MockTestResultUiEffect.Close -> onBack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(MockTestResultUiAction.Load)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        QrizTopBar(
            title = if (state.viewType == MockTestResultUiState.ViewType.TOTAL) stringResource(R.string.mock_test_result) else null,
            navigationType = if (state.viewType == MockTestResultUiState.ViewType.DETAIL) NavigationType.BACK else NavigationType.CLOSE,
            onNavigationClick = { viewModel.process(MockTestResultUiAction.OnClickBackButton) },
        )

        when (val loadState = state.loadState) {
            is MockTestResultUiState.LoadState.Loading -> QrizLoading()
            is MockTestResultUiState.LoadState.Failure -> ErrorScreen(
                title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
                description = loadState.message,
                onClickRetry = { viewModel.process(MockTestResultUiAction.Load) })

            is MockTestResultUiState.LoadState.Success -> MockTestResultContent(
                userName = state.userName,
                testResult = loadState.result,
                viewType = state.viewType,
                showDetailSubjectFilter = state.showDetailFilterDropdown,
                selectedSubjectFilter = state.selectedSubjectFilter,
                showHistoryFilterDropDown = state.showHistoryFilterDropdown,
                onSelectFilter = { viewModel.process(MockTestResultUiAction.OnSelectFilter(it)) },
                onClickDetail = { viewModel.process(MockTestResultUiAction.OnClickDetail) },
                onClickFilter = { viewModel.process(MockTestResultUiAction.ShowDetailFilterDropdown) },
                onClickHistoryFilter = { viewModel.process(MockTestResultUiAction.OnClickHistoryFilter) },
                onChangeHistoryFilter = { viewModel.process(MockTestResultUiAction.OnChangeHistoryFilter(it)) },
                onBack = { viewModel.process(MockTestResultUiAction.OnClickBackButton) },
            )
        }
    }
}

@Composable
internal fun MockTestResultContent(
    userName: String,
    selectedSubjectFilter: ScoreDetailSubjectFilter,
    showDetailSubjectFilter: Boolean,
    testResult: MockTestResultItem,
    viewType: MockTestResultUiState.ViewType,
    showHistoryFilterDropDown: Boolean,
    onClickDetail: () -> Unit,
    onClickFilter: () -> Unit,
    onSelectFilter: (ScoreDetailSubjectFilter) -> Unit,
    onClickHistoryFilter: () -> Unit,
    onChangeHistoryFilter: (MockTestResultUiState.HistoricalScoreFilter) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    when (viewType) {
        MockTestResultUiState.ViewType.TOTAL -> MockTestTotalScorePage(
            modifier = Modifier.fillMaxSize(),
            userName = userName,
            totalScore = testResult.totalScores[ScoreDetailSubjectFilter.TOTAL]!!,
            testResultItems = testResult.skillScores,
            questionResultItems = testResult.questionResults,
            historicalResultItem = testResult.filterHistoricalResults,
            historicalScoreFilter = testResult.historicalScoreFilter,
            showFilterDropDown = showHistoryFilterDropDown,
            getTestResultColor = testResult::getScoreColor,
            onClickHistoryFilter = onClickHistoryFilter,
            onChangeHistoryFilter = onChangeHistoryFilter,
            onClickDetail = onClickDetail,
        )

        MockTestResultUiState.ViewType.DETAIL -> MockTestDetailScorePage(
            selectedSubjectFilter = selectedSubjectFilter,
            showFilterDropDown = showDetailSubjectFilter,
            totalScore = testResult.totalScores[selectedSubjectFilter]!!,
            testResultItem = testResult.scoreDetails[selectedSubjectFilter]!!.map {
                TestResultItem(
                    scoreName = it.name,
                    score = it.score
                )
            }.toImmutableList(),
            resultDetailItems = testResult.scoreDetails[selectedSubjectFilter]!!,
            getTestResultColor = testResult::getScoreColor,
            onSelectFilter = onSelectFilter,
            onClickFilter = onClickFilter,
        )
    }
}

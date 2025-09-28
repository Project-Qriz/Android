package com.qriz.app.feature.mock_test.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestResult
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.requireValue
import com.qriz.app.core.navigation.route.MockTestRoute
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.mock_test.result.mapper.toResultItem
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MockTestResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MockTestRepository,
    private val userRepository: UserRepository,
) : BaseViewModel<MockTestResultUiState, MockTestResultUiEffect, MockTestResultUiAction>(
    MockTestResultUiState.DEFAULT
) {
    private val sessionId = savedStateHandle.toRoute<MockTestRoute.MockTestResult>().id

    override fun process(action: MockTestResultUiAction): Job = viewModelScope.launch {
        when (action) {
            is MockTestResultUiAction.Load -> loadData()
            is MockTestResultUiAction.OnClickClose -> sendEffect(MockTestResultUiEffect.Close)
            is MockTestResultUiAction.OnClickDetail -> updateState { copy(viewType = MockTestResultUiState.ViewType.DETAIL) }
            is MockTestResultUiAction.ShowDetailFilterDropdown -> updateState { copy(showDetailFilterDropdown = true) }
            is MockTestResultUiAction.ShowHistoryFilterDropdown -> {}
            is MockTestResultUiAction.DismissDetailFilterDropdown -> updateState { copy(showDetailFilterDropdown = false) }
            is MockTestResultUiAction.DismissHistoryFilterDropdown -> {}
            is MockTestResultUiAction.OnClickBackButton -> handleClickBackButton()
            is MockTestResultUiAction.OnSelectFilter -> onSelectDetailFilter(action.filter)
            is MockTestResultUiAction.OnChangeHistoryFilter -> onSelectHistoricalScoreFilter(action.filter)
            is MockTestResultUiAction.OnClickHistoryFilter -> updateState { copy(showHistoryFilterDropdown = showHistoryFilterDropdown.not()) }
        }
    }

    private suspend fun loadData() {
        val user = userRepository.getUser().requireValue
        when (val result = repository.getMockTestResult(sessionId)) {
            is ApiResult.Success<MockTestResult> -> updateState {
                copy(
                    loadState = MockTestResultUiState.LoadState.Success(result.data.toResultItem()),
                    userName = user.name,
                )
            }

            is ApiResult.Failure -> updateState {
                copy(
                    loadState = MockTestResultUiState.LoadState.Failure(result.message)
                )
            }

            is ApiResult.NetworkError -> updateState {
                copy(
                    loadState = MockTestResultUiState.LoadState.Failure(NETWORK_IS_UNSTABLE)
                )
            }

            is ApiResult.UnknownError -> updateState {
                copy(
                    loadState = MockTestResultUiState.LoadState.Failure(UNKNOWN_ERROR)
                )
            }
        }
    }

    private fun handleClickBackButton() {
        val currentViewType = uiState.value.viewType
        when (currentViewType) {
            MockTestResultUiState.ViewType.TOTAL -> {
                sendEffect(MockTestResultUiEffect.Close)
            }

            MockTestResultUiState.ViewType.DETAIL -> {
                updateState {
                    copy(
                        viewType = MockTestResultUiState.ViewType.TOTAL,
                        showDetailFilterDropdown = false,
                        selectedSubjectFilter = ScoreDetailSubjectFilter.TOTAL,
                    )
                }
            }
        }
    }

    private fun onSelectDetailFilter(filter: ScoreDetailSubjectFilter) {
        updateState {
            copy(
                showDetailFilterDropdown = false,
                selectedSubjectFilter = filter,
            )
        }
    }

    private fun onSelectHistoricalScoreFilter(filter: MockTestResultUiState.HistoricalScoreFilter) {
        val currentLoadState = uiState.value.loadState

        if (currentLoadState !is MockTestResultUiState.LoadState.Success) {
            return
        }

        updateState {
            copy(
                showHistoryFilterDropdown = false,
                loadState = currentLoadState.copy(result = currentLoadState.result.copy(historicalScoreFilter = filter))
            )
        }
    }
}

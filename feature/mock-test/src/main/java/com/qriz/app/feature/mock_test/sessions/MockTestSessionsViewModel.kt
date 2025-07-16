package com.qriz.app.feature.mock_test.sessions

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.mock_test.sessions.model.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MockTestSessionsViewModel @Inject constructor(
    private val mockTestRepository: MockTestRepository,
) : BaseViewModel<MockTestSessionsUiState, MockTestSessionsUiEffect, MockTestSessionsUiAction>(
    MockTestSessionsUiState.DEFAULT
) {

    override fun process(action: MockTestSessionsUiAction): Job = viewModelScope.launch {
        when (action) {
            is MockTestSessionsUiAction.LoadData -> loadData()
        }
    }

    private suspend fun loadData() {
        when (val result = mockTestRepository.getMockTestSessions()) {
            is ApiResult.Success<List<MockTestSession>> -> updateState { copy(sessionState = SessionState.Success(result.data.toImmutableList())) }
            is ApiResult.Failure -> updateState { copy(sessionState = SessionState.Failure(message = result.message)) }
            is ApiResult.NetworkError -> updateState { copy(sessionState = SessionState.Failure(message = NETWORK_IS_UNSTABLE)) }
            is ApiResult.UnknownError -> updateState { copy(sessionState = SessionState.Failure(message = UNKNOWN_ERROR)) }
        }
    }
}

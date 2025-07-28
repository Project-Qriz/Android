package com.qriz.app.feature.mock_test.sessions

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.mock_test.sessions.model.SessionState

@Immutable
data class MockTestSessionsUiState(
    val sessionState: SessionState,
    val filter: SessionFilter,
    val expandFilter: Boolean,
) : UiState {
    companion object {
        val DEFAULT = MockTestSessionsUiState(
            sessionState = SessionState.Loading,
            filter = SessionFilter.ALL,
            expandFilter = false,
        )
    }
}

sealed interface MockTestSessionsUiAction : UiAction {
    data class ClickMockTest(val id: Long) : MockTestSessionsUiAction
    data object ClickSessionFilter : MockTestSessionsUiAction
    data class SelectSessionFilter(val filter: SessionFilter) : MockTestSessionsUiAction
}

sealed interface MockTestSessionsUiEffect : UiEffect {
    data class ShowSnackBar(val message: String) : MockTestSessionsUiEffect
    data class MoveToMockTest(val id: Long) : MockTestSessionsUiEffect
}

package com.qriz.app.feature.mock_test.sessions

import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.mock_test.sessions.model.Filter
import com.qriz.app.feature.mock_test.sessions.model.SessionState

@Immutable
data class MockTestSessionsUiState(
    val sessionState: SessionState,
    val filter: Filter,
) : UiState {
    companion object {
        val DEFAULT = MockTestSessionsUiState(
            sessionState = SessionState.Loading,
            filter = Filter.ALL,
        )
    }
}

sealed interface MockTestSessionsUiAction : UiAction {
    data object LoadData : MockTestSessionsUiAction
}

sealed interface MockTestSessionsUiEffect : UiEffect {
    data class ShowSnackBar(val message: String) : MockTestSessionsUiEffect
}

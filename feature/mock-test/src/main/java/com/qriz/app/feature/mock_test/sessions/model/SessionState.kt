package com.qriz.app.feature.mock_test.sessions.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface SessionState {
    @Immutable
    data object Loading : SessionState

    @Immutable
    data class Success(val data: ImmutableList<MockTestSession>) : SessionState

    @Immutable
    data class Failure(val message: String) : SessionState
}

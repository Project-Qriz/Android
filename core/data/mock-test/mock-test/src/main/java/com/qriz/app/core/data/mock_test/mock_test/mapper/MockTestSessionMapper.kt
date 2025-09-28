package com.qriz.app.core.data.mock_test.mock_test.mapper

import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.network.mock_test.model.response.MockTestSessionResponse

fun List<MockTestSessionResponse>.toMockTestSession(): List<MockTestSession> {
    return this.map {
        MockTestSession(
            completed = it.completed,
            session = it.session,
            totalScore = if (it.totalScore != null) it.totalScore!!.toInt() else 0,
            id = it.id
        )
    }
}

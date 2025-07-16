package com.qriz.app.core.data.mock_test.mock_test_api.repository

import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult

interface MockTestRepository {
    suspend fun getMockTestSessions(completed: Boolean? = null): ApiResult<List<MockTestSession>>
    suspend fun getMockTest(id: Long): ApiResult<Test>
}

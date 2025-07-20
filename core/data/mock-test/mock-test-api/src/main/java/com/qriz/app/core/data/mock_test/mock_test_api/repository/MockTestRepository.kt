package com.qriz.app.core.data.mock_test.mock_test_api.repository

import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface MockTestRepository {
    val mockTestSessions: Flow<ApiResult<List<MockTestSession>>>

    fun setSessionFilter(filter: SessionFilter)

    suspend fun getMockTest(id: Long): ApiResult<Test>
}

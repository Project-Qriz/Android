package com.qriz.app.core.data.mock_test.mock_test.repository

import com.qriz.app.core.data.mock_test.mock_test.mapper.toMockTestSession
import com.qriz.app.core.data.mock_test.mock_test.mapper.toTest
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.map
import com.qriz.app.core.network.mock_test.model.api.MockTestApi
import javax.inject.Inject

class MockTestRepositoryImpl @Inject constructor(
    private val mockTestApi: MockTestApi,
): MockTestRepository {
    override suspend fun getMockTestSessions(completed: Boolean?): ApiResult<List<MockTestSession>> {
        return mockTestApi.getMockTestSessions(completed).map { it.toMockTestSession() }
    }

    override suspend fun getMockTest(id: Long): ApiResult<Test> {
        return mockTestApi.getMockTestQuestions(id).map { it.toTest() }
    }
}

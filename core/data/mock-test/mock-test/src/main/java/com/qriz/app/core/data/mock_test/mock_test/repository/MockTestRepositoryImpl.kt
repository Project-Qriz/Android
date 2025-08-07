package com.qriz.app.core.data.mock_test.mock_test.repository

import com.qriz.app.core.data.mock_test.mock_test.mapper.toMockTestSession
import com.qriz.app.core.data.mock_test.mock_test.mapper.toRequest
import com.qriz.app.core.data.mock_test.mock_test.mapper.toResult
import com.qriz.app.core.data.mock_test.mock_test.mapper.toTest
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestResult
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSubjectScore
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.flatMapSuspend
import com.qriz.app.core.model.map
import com.qriz.app.core.network.mock_test.api.MockTestApi
import com.qriz.app.core.network.mock_test.model.response.MockTestResultResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestScoreResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class MockTestRepositoryImpl @Inject constructor(
    private val mockTestApi: MockTestApi,
) : MockTestRepository {

    private val sessionFilter = MutableStateFlow(SessionFilter.ALL)

    private val _mockTestSessions =
        MutableStateFlow<ApiResult<List<MockTestSession>>>(ApiResult.Success(emptyList()))

    @OptIn(ExperimentalCoroutinesApi::class)
    override val mockTestSessions: Flow<ApiResult<List<MockTestSession>>> =
        _mockTestSessions.onStart {
            if (_mockTestSessions.value is ApiResult.Success && (_mockTestSessions.value as ApiResult.Success).data.isNotEmpty()) {
                return@onStart
            }
            fetchMockTestSessions()
        }.flatMapLatest { result ->
            sessionFilter.map { filter ->
                result.map { sessions ->
                    when (filter) {
                        SessionFilter.ALL -> sessions
                        SessionFilter.COMPLETED -> sessions.filter { session -> session.completed }
                        SessionFilter.NOT_COMPLETED -> sessions.filter { session -> session.completed.not() }
                        SessionFilter.OLDEST_FIRST -> sessions.sortedBy { session -> session.id }
                    }
                }
            }
        }

    override fun setSessionFilter(filter: SessionFilter) {
        sessionFilter.value = filter
    }

    private suspend fun fetchMockTestSessions() {
        val result = mockTestApi.getMockTestSessions(null).map { it.toMockTestSession().reversed() }
        _mockTestSessions.value = result
    }

    override suspend fun getMockTest(id: Long): ApiResult<Test> {
        return mockTestApi.getMockTestQuestions(id).map { it.toTest() }
    }

    override suspend fun submitMockTest(id: Long, activities: Map<Long, Option>): ApiResult<Unit> {
        val request = activities.toRequest()
        return mockTestApi.submitMockTest(
            id = id,
            request = request
        )
    }

    // TODO: ApiResult를 사용하게 되면 동시에 api 호출하는 게 굉장히 어려워짐 -> 리팩토링이 필요할 듯
    override suspend fun getMockTestResult(id: Long): ApiResult<MockTestResult> {
        return mockTestApi.getMockTestResult(id).flatMapSuspend { result ->
                mockTestApi.getMockTestSubjectDetails(id = id).map { scores ->
                    toResult(result, scores)
                }
            }
    }
}

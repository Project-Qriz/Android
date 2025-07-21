package com.qriz.app.core.data.mock_test.mock_test

import app.cash.turbine.test
import com.qriz.app.core.data.mock_test.mock_test.repository.MockTestRepositoryImpl
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.Test as TestModel
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.mock_test.model.api.MockTestApi
import com.qriz.app.core.network.mock_test.model.response.MockTestOptionResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestQuestionResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestQuestionsResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestSessionResponse
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MockTestRepositoryTest {
    private val mockApi = mockk<MockTestApi>()
    private val repository = MockTestRepositoryImpl(mockApi)

    private val mockSessionResponses = listOf(
        MockTestSessionResponse(
            completed = true,
            session = "1회차",
            totalScore = 85
        ),
        MockTestSessionResponse(
            completed = false,
            session = "2회차",
            totalScore = null
        ),
        MockTestSessionResponse(
            completed = true,
            session = "3회차",
            totalScore = 92
        )
    )

    private val mockSessions = listOf(
        MockTestSession(
            completed = true,
            session = "3회차",
            totalScore = 92
        ),
        MockTestSession(
            completed = false,
            session = "2회차",
            totalScore = 0
        ),
        MockTestSession(
            completed = true,
            session = "1회차",
            totalScore = 85
        )
    )

    @Test
    fun `mockTestSessions flow - 초기 데이터 로딩 및 ALL 필터 적용`() = runTest {
        // given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(mockSessionResponses)

        // when & then
        repository.mockTestSessions.test {
            val result = awaitItem()
            result shouldBe ApiResult.Success(mockSessions)
            coVerify { mockApi.getMockTestSessions(null) }
        }
    }

    @Test
    fun `mockTestSessions flow - API 실패 시 실패 결과 반환`() = runTest {
        // given
        val errorResult = ApiResult.Failure(
            -1,
            "API 오류"
        )
        coEvery { mockApi.getMockTestSessions(null) } returns errorResult

        // when & then
        repository.mockTestSessions.test {
            val result = awaitItem()
            result shouldBe errorResult
            coVerify { mockApi.getMockTestSessions(null) }
        }
    }

    @Test
    fun `setSessionFilter - COMPLETED 필터 적용`() = runTest {
        // given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(mockSessionResponses)

        // when & then
        repository.mockTestSessions.test {
            // 초기 데이터 (ALL 필터)
            val initialResult = awaitItem()
            initialResult shouldBe ApiResult.Success(mockSessions)

            // COMPLETED 필터 적용
            repository.setSessionFilter(SessionFilter.COMPLETED)
            val filteredResult = awaitItem()

            val expectedCompleted = mockSessions.filter { it.completed }
            filteredResult shouldBe ApiResult.Success(expectedCompleted)
        }
    }

    @Test
    fun `setSessionFilter - NOT_COMPLETED 필터 적용`() = runTest {
        // given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(mockSessionResponses)

        // when & then
        repository.mockTestSessions.test {
            // 초기 데이터 (ALL 필터)
            awaitItem()

            // NOT_COMPLETED 필터 적용
            repository.setSessionFilter(SessionFilter.NOT_COMPLETED)
            val filteredResult = awaitItem()

            val expectedNotCompleted = mockSessions.filter { !it.completed }
            filteredResult shouldBe ApiResult.Success(expectedNotCompleted)
        }
    }

    @Test
    fun `setSessionFilter - OLDEST_FIRST 필터 적용`() = runTest {
        // given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(mockSessionResponses)

        // when & then
        repository.mockTestSessions.test {
            // 초기 데이터 (ALL 필터)
            awaitItem()

            // OLDEST_FIRST 필터 적용
            repository.setSessionFilter(SessionFilter.OLDEST_FIRST)
            val filteredResult = awaitItem()

            val expectedSorted = mockSessions.sortedBy {
                it.session.replace(
                    "회차",
                    ""
                ).toInt()
            }
            filteredResult shouldBe ApiResult.Success(expectedSorted)
        }
    }

    @Test
    fun `setSessionFilter - 여러 필터 순차 적용`() = runTest {
        // given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(mockSessionResponses)

        // when & then
        repository.mockTestSessions.test {
            // 초기 데이터 (ALL 필터)
            val allResult = awaitItem()
            allResult shouldBe ApiResult.Success(mockSessions)

            // COMPLETED 필터 적용
            repository.setSessionFilter(SessionFilter.COMPLETED)
            val completedResult = awaitItem()
            val expectedCompleted = mockSessions.filter { it.completed }
            completedResult shouldBe ApiResult.Success(expectedCompleted)

            // 다시 ALL 필터로 변경
            repository.setSessionFilter(SessionFilter.ALL)
            val backToAllResult = awaitItem()
            backToAllResult shouldBe ApiResult.Success(mockSessions)
        }
    }

    @Test
    fun `mockTestSessions flow - 이미 데이터가 있으면 API 재호출 안함`() = runTest {
        // given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(mockSessionResponses)

        // when - 첫 번째 구독
        repository.mockTestSessions.test {
            awaitItem()
        }

        // when - 두 번째 구독 (데이터가 이미 있음)
        repository.mockTestSessions.test {
            val result = awaitItem()
            result shouldBe ApiResult.Success(mockSessions)
        }

        // then - API는 한 번만 호출되어야 함
        coVerify(exactly = 1) { mockApi.getMockTestSessions(null) }
    }

    @Test
    fun `getMockTest - 모의고사 문제를 성공적으로 불러와서 Test로 변환한다`() = runTest {
        // given
        val mockTestId = 1L
        val mockResponse = MockTestQuestionsResponse(
            questions = listOf(
                MockTestQuestionResponse(
                    questionId = 1L,
                    skillId = 1L,
                    category = 1,
                    question = "SQL의 기본 문법에 대한 설명으로 옳은 것은?",
                    description = "SQL 기본 문법을 묻는 문제입니다.",
                    options = listOf(
                        MockTestOptionResponse(
                            id = 1L,
                            content = "SELECT는 데이터를 조회한다"
                        ),
                        MockTestOptionResponse(
                            id = 2L,
                            content = "INSERT는 데이터를 삽입한다"
                        ),
                        MockTestOptionResponse(
                            id = 3L,
                            content = "UPDATE는 데이터를 수정한다"
                        ),
                        MockTestOptionResponse(
                            id = 4L,
                            content = "모든 답이 맞다"
                        )
                    ),
                    timeLimit = 120,
                    difficulty = 2
                ),
                MockTestQuestionResponse(
                    questionId = 2L,
                    skillId = 2L,
                    category = 2,
                    question = "다음 중 올바른 JOIN 문법은?",
                    description = "JOIN 문법을 묻는 문제입니다.",
                    options = listOf(
                        MockTestOptionResponse(
                            id = 5L,
                            content = "INNER JOIN"
                        ),
                        MockTestOptionResponse(
                            id = 6L,
                            content = "LEFT JOIN"
                        ),
                        MockTestOptionResponse(
                            id = 7L,
                            content = "RIGHT JOIN"
                        ),
                        MockTestOptionResponse(
                            id = 8L,
                            content = "모든 답이 맞다"
                        )
                    ),
                    timeLimit = 90,
                    difficulty = 1
                )
            ),
            totalTimeLimit = 210
        )

        coEvery { mockApi.getMockTestQuestions(mockTestId) } returns ApiResult.Success(mockResponse)

        val expected = TestModel(
            questions = listOf(
                Question(
                    id = 1L,
                    question = "SQL의 기본 문법에 대한 설명으로 옳은 것은?",
                    options = listOf(
                        Option(
                            id = 1L,
                            content = "SELECT는 데이터를 조회한다"
                        ),
                        Option(
                            id = 2L,
                            content = "INSERT는 데이터를 삽입한다"
                        ),
                        Option(
                            id = 3L,
                            content = "UPDATE는 데이터를 수정한다"
                        ),
                        Option(
                            id = 4L,
                            content = "모든 답이 맞다"
                        )
                    ),
                    timeLimit = 120,
                    description = "SQL 기본 문법을 묻는 문제입니다.",
                    skillId = 1,
                    category = 1,
                    difficulty = 2
                ),
                Question(
                    id = 2L,
                    question = "다음 중 올바른 JOIN 문법은?",
                    options = listOf(
                        Option(
                            id = 5L,
                            content = "INNER JOIN"
                        ),
                        Option(
                            id = 6L,
                            content = "LEFT JOIN"
                        ),
                        Option(
                            id = 7L,
                            content = "RIGHT JOIN"
                        ),
                        Option(
                            id = 8L,
                            content = "모든 답이 맞다"
                        )
                    ),
                    timeLimit = 90,
                    description = "JOIN 문법을 묻는 문제입니다.",
                    skillId = 2,
                    category = 2,
                    difficulty = 1
                )
            ),
            totalTimeLimit = 210
        )

        // when
        val result = repository.getMockTest(mockTestId)

        // then
        coVerify { mockApi.getMockTestQuestions(mockTestId) }
        result shouldBe ApiResult.Success(expected)
    }

    @Test
    fun `getMockTest - API 실패 시 실패 결과 반환`() = runTest {
        // given
        val mockTestId = 1L
        val errorResult = ApiResult.Failure(
            -1,
            "문제 조회 실패"
        )
        coEvery { mockApi.getMockTestQuestions(mockTestId) } returns errorResult

        // when
        val result = repository.getMockTest(mockTestId)

        // then
        coVerify { mockApi.getMockTestQuestions(mockTestId) }
        result shouldBe errorResult
    }
}

package com.qriz.app.core.data.mock_test.mock_test

import com.qriz.app.core.data.mock_test.mock_test.repository.MockTestRepositoryImpl
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
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

    @Test
    fun `모의고사 세션 목록을 성공적으로 불러온다`() = runTest {
        //given
        coEvery { mockApi.getMockTestSessions(null) } returns ApiResult.Success(
            listOf(
                MockTestSessionResponse(
                    completed = true,
                    session = "session1",
                    totalScore = 85
                ),
                MockTestSessionResponse(
                    completed = false,
                    session = "session2",
                    totalScore = null
                )
            )
        )

        val expected = listOf(
            MockTestSession(
                completed = true,
                session = "session1",
                totalScore = 85
            ),
            MockTestSession(
                completed = false,
                session = "session2",
                totalScore = 0
            )
        )

        //when
        val result = repository.getMockTestSessions()

        //then
        coVerify { mockApi.getMockTestSessions(null) }
        result shouldBe ApiResult.Success(expected)
    }

    @Test
    fun `완료된 모의고사 세션만 필터링해서 불러온다`() = runTest {
        //given
        coEvery { mockApi.getMockTestSessions(true) } returns ApiResult.Success(
            listOf(
                MockTestSessionResponse(
                    completed = true,
                    session = "session1",
                    totalScore = 85
                )
            )
        )

        val expected = listOf(
            MockTestSession(
                completed = true,
                session = "session1",
                totalScore = 85
            )
        )

        //when
        val result = repository.getMockTestSessions(completed = true)

        //then
        coVerify { mockApi.getMockTestSessions(true) }
        result shouldBe ApiResult.Success(expected)
    }

    @Test
    fun `미완료된 모의고사 세션만 필터링해서 불러온다`() = runTest {
        //given
        coEvery { mockApi.getMockTestSessions(false) } returns ApiResult.Success(
            listOf(
                MockTestSessionResponse(
                    completed = false,
                    session = "session2",
                    totalScore = null
                )
            )
        )

        val expected = listOf(
            MockTestSession(
                completed = false,
                session = "session2",
                totalScore = 0
            )
        )

        //when
        val result = repository.getMockTestSessions(completed = false)

        //then
        coVerify { mockApi.getMockTestSessions(false) }
        result shouldBe ApiResult.Success(expected)
    }

    @Test
    fun `모의고사 문제를 성공적으로 불러와서 Test로 변환한다`() = runTest {
        //given
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

        //when
        val result = repository.getMockTest(mockTestId)

        //then
        coVerify { mockApi.getMockTestQuestions(mockTestId) }
        result shouldBe ApiResult.Success(expected)
    }
}

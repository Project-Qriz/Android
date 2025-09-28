package com.qriz.app.feature.mock_test

import app.cash.turbine.test
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.mock_test.sessions.MockTestSessionsUiAction
import com.qriz.app.feature.mock_test.sessions.MockTestSessionsViewModel
import com.qriz.app.feature.mock_test.sessions.model.SessionState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockTestSessionsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMockTestRepository = mockk<MockTestRepository>(relaxed = true)

    private fun viewModel(): MockTestSessionsViewModel {
        return MockTestSessionsViewModel(
            mockTestRepository = mockMockTestRepository
        )
    }

    private val mockSessions = listOf(
        MockTestSession(
            completed = true,
            session = "1회차",
            totalScore = 85,
            id = 1,
        ),
        MockTestSession(
            completed = false,
            session = "2회차",
            totalScore = 0,
            id = 2,
        ),
        MockTestSession(
            completed = true,
            session = "3회차",
            totalScore = 92,
            id = 3,
        )
    )

    @Test
    fun `초기 상태 확인`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf()

        // when
        with(viewModel()) {
            // then
            val state = uiState.value
            assertEquals(
                SessionState.Loading,
                state.sessionState
            )
            assertEquals(
                SessionFilter.ALL,
                state.filter
            )
            assertFalse(state.expandFilter)
        }
    }

    @Test
    fun `mockTestSessions success - sessionState Success 상태 변경`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Success(mockSessions)
        )

        // when
        with(viewModel()) {
            // then
            uiState.test {
                val state = awaitItem()
                assertTrue(state.sessionState is SessionState.Success)
                assertEquals(
                    mockSessions.toImmutableList(),
                    (state.sessionState as SessionState.Success).data
                )
                assertEquals(
                    SessionFilter.ALL,
                    state.filter
                )
                assertFalse(state.expandFilter)
            }
        }
    }

    @Test
    fun `mockTestSessions failure - sessionState Failure 상태 변경`() = runTest {
        // given
        val fakeErrorMessage = "API 오류"
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Failure(
                code = -1,
                message = fakeErrorMessage
            )
        )

        // when
        with(viewModel()) {
            // then
            uiState.test {
                val state = awaitItem()
                assertTrue(state.sessionState is SessionState.Failure)
                assertEquals(
                    fakeErrorMessage,
                    (state.sessionState as SessionState.Failure).message
                )
            }
        }
    }

    @Test
    fun `mockTestSessions networkError - sessionState Failure 상태 변경`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.NetworkError(IOException())
        )

        // when
        with(viewModel()) {
            // then
            uiState.test {
                val state = awaitItem()
                assertTrue(state.sessionState is SessionState.Failure)
                assertEquals(
                    NETWORK_IS_UNSTABLE,
                    (state.sessionState as SessionState.Failure).message
                )
            }
        }
    }

    @Test
    fun `mockTestSessions unknownError - sessionState Failure 상태 변경`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.UnknownError(Exception())
        )

        // when
        with(viewModel()) {
            // then
            uiState.test {
                val state = awaitItem()
                assertTrue(state.sessionState is SessionState.Failure)
                assertEquals(
                    UNKNOWN_ERROR,
                    (state.sessionState as SessionState.Failure).message
                )
            }
        }
    }

    @Test
    fun `ClickSessionFilter process - expandFilter 상태 토글`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Success(mockSessions)
        )

        with(viewModel()) {
            // when
            process(MockTestSessionsUiAction.ClickSessionFilter)

            // then
            uiState.test {
                val state = awaitItem()
                assertTrue(state.expandFilter)
            }

            // when
            process(MockTestSessionsUiAction.ClickSessionFilter)

            // then
            uiState.test {
                val state = awaitItem()
                assertFalse(state.expandFilter)
            }
        }
    }

    @Test
    fun `SelectSessionFilter process - filter 변경 및 expandFilter false 상태 변경`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Success(mockSessions)
        )

        with(viewModel()) {
            // when
            process(MockTestSessionsUiAction.SelectSessionFilter(SessionFilter.COMPLETED))

            // then
            uiState.test {
                val state = awaitItem()
                assertEquals(
                    SessionFilter.COMPLETED,
                    state.filter
                )
                assertFalse(state.expandFilter)
            }
            verify { mockMockTestRepository.setSessionFilter(SessionFilter.COMPLETED) }
        }
    }

    @Test
    fun `SelectSessionFilter process - 모든 필터 옵션 테스트`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Success(mockSessions)
        )

        with(viewModel()) {
            val filterOptions = listOf(
                SessionFilter.ALL,
                SessionFilter.COMPLETED,
                SessionFilter.NOT_COMPLETED,
                SessionFilter.OLDEST_FIRST
            )

            // when & then
            filterOptions.forEach { filter ->
                process(MockTestSessionsUiAction.SelectSessionFilter(filter))

                uiState.test {
                    val state = awaitItem()
                    assertEquals(
                        filter,
                        state.filter
                    )
                    assertFalse(state.expandFilter)
                }
                verify { mockMockTestRepository.setSessionFilter(filter) }
            }
        }
    }

    @Test
    fun `ClickMockTest process - 현재는 아무 동작 없음`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Success(mockSessions)
        )

        with(viewModel()) {
            val initialState = uiState.value

            // when
            process(MockTestSessionsUiAction.ClickMockTest(1L))

            // then
            val currentState = uiState.value
            assertEquals(
                initialState,
                currentState
            )
        }
    }

    @Test
    fun `expandFilter가 true인 상태에서 SelectSessionFilter process - expandFilter false 변경`() = runTest {
        // given
        coEvery { mockMockTestRepository.mockTestSessions } returns flowOf(
            ApiResult.Success(mockSessions)
        )

        with(viewModel()) {
            // expandFilter를 true로 설정
            process(MockTestSessionsUiAction.ClickSessionFilter)

            // when
            process(MockTestSessionsUiAction.SelectSessionFilter(SessionFilter.COMPLETED))

            // then
            uiState.test {
                val state = awaitItem()
                assertEquals(
                    SessionFilter.COMPLETED,
                    state.filter
                )
                assertFalse(state.expandFilter) // false로 변경되어야 함
            }
        }
    }
}

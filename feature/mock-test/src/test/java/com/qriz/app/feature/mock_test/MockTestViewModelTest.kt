package com.qriz.app.feature.mock_test

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.MockTestRoute
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.testing.mockkToRoute
import com.qriz.app.core.ui.test.mapper.toGeneralOptionItem
import com.qriz.app.core.ui.test.mapper.toQuestionTestItem
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import com.qriz.app.feature.mock_test.test.MockTestUiAction
import com.qriz.app.feature.mock_test.test.MockTestUiEffect
import com.qriz.app.feature.mock_test.test.MockTestUiState
import com.qriz.app.feature.mock_test.test.MockTestViewModel
import com.qriz.app.feature.mock_test.test.MockTestViewModel.Companion.toMilliSecond
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.qriz.app.core.data.test.test_api.model.Test as TestModel

class MockTestViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeMockTestRepository = mockk<MockTestRepository>()

    private lateinit var mockTestViewModel: MockTestViewModel

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.mockkToRoute(
            MockTestRoute.MockTest(id = 1)
        )

        mockTestViewModel = MockTestViewModel(
            savedStateHandle = savedStateHandle,
            mockTestRepository = fakeMockTestRepository
        )
    }

    @Test
    fun `Action_ObserveMockTestItem process 성공 - 상태 업데이트, 타이머 시작`() = runTest {
        // given
        coEvery { fakeMockTestRepository.getMockTest(1L) } returns ApiResult.Success(fakeTest)
        coEvery { fakeMockTestRepository.submitMockTest(1L, any()) } returns ApiResult.Success(Unit)
        with(mockTestViewModel) {

            // when
            process(MockTestUiAction.ObserveMockTestItem)

            // then
            uiState.test {
                with(awaitItem()) {
                    assertTrue(state is MockTestUiState.LoadState.Success)
                    val successState = state as MockTestUiState.LoadState.Success
                    assertEquals(fakeTest.questions.toQuestionTestItem(emptyMap())
                        .toImmutableList(), successState.questions)
                    assertEquals(fakeTest.totalTimeLimit.toMilliSecond(), remainTimeMs)
                    assertEquals(fakeTest.totalTimeLimit.toMilliSecond(), totalTimeLimitMs)
                }
                assertNotNull(timerJob)
            }
        }
    }

    @Test
    fun `Action_ObserveMockTestItem process 실패 - LoadState_Failure 상태, 타이머 시작되지 않음`() =
        runTest {
            // given
            coEvery { fakeMockTestRepository.getMockTest(1L) } returns ApiResult.Failure(code = -1, message = "Error")
            
            with(mockTestViewModel) {
                
                // when
                process(MockTestUiAction.ObserveMockTestItem)
                
                //then
                uiState.test { 
                    with(awaitItem()) {
                        assertTrue(state is MockTestUiState.LoadState.Failure)
                    }
                }
                assertNull(timerJob)
            }
        }

    @Test
    fun `Action_SelectOption process - 해당 질문의 선택된 옵션이 업데이트 됨`() = runTest {
        // given
        coEvery { fakeMockTestRepository.getMockTest(1L) } returns ApiResult.Success(fakeTest)
        coEvery { fakeMockTestRepository.submitMockTest(1L, any()) } returns ApiResult.Success(Unit)

                with(mockTestViewModel) {
            val selectedQuestion = fakeTest.questions.first()
            val selectedOption1 = selectedQuestion.options.first().toGeneralOptionItem()
            process(MockTestUiAction.ObserveMockTestItem)
            process(
                MockTestUiAction.SelectOption(
                    questionID = selectedQuestion.id,
                    option = selectedOption1
                )
            )

            // when & then
            uiState.test {
                val state = awaitItem().state as MockTestUiState.LoadState.Success
                val targetQuestion = state.questions.first { it.id == selectedQuestion.id }
                assertTrue(targetQuestion.options.first() is SelectedOrCorrectOptionItem)
                assertEquals(1, targetQuestion.options.count { it is SelectedOrCorrectOptionItem })
                assertEquals(targetQuestion.options.size - 1, targetQuestion.options.count { it is GeneralOptionItem })
            }

            // given
            val selectedOption2 = selectedQuestion.options.last().toGeneralOptionItem()
            process(
                MockTestUiAction.SelectOption(
                    questionID = selectedQuestion.id,
                    option = selectedOption2
                )
            )

            // when & then
            uiState.test {
                val state = awaitItem().state as MockTestUiState.LoadState.Success
                val targetQuestion = state.questions.first { it.id == selectedQuestion.id }
                assertTrue(targetQuestion.options.last() is SelectedOrCorrectOptionItem)
                assertEquals(1, targetQuestion.options.count { it is SelectedOrCorrectOptionItem })
                assertEquals(targetQuestion.options.size - 1, targetQuestion.options.count { it is GeneralOptionItem })
            }
        }
    }

    @Test
    fun `Action_ClickNextPage process - 페이지 인덱스가 1씩 증가함`() = runTest {
        with(mockTestViewModel) {
            // given
            process(MockTestUiAction.ClickNextPage)

            // when & then
            uiState.test {
                assertEquals(MockTestUiState.Default.currentIndex + 1, awaitItem().currentIndex)
            }
        }
    }

    @Test
    fun `Action_ClickPreviousPage process 첫 페이지가 아님 - 페이지 인덱스가 1씩 감소함`() = runTest {
        with(mockTestViewModel) {
            // given
            val clickCount = 3
            (1..clickCount).forEach { process(MockTestUiAction.ClickNextPage) }
            process(MockTestUiAction.ClickPreviousPage)

            // when & then
            uiState.test { assertEquals(clickCount - 1, awaitItem().currentIndex) }
        }
    }

    @Test
    fun `Action_ClickPreviousPage process 첫 페이지 - 페이지 감소 x, isVisibleTestEndWarningDialog = true`() =
        runTest {
            with(mockTestViewModel) {
                // given
                process(MockTestUiAction.ClickPreviousPage)

                // when & then
                uiState.test {
                    with(awaitItem()) {
                        assertEquals(0, currentIndex)
                        assertTrue(isVisibleTestEndWarningDialog)
                    }
                }
            }
        }

    @Test
    fun `Action_ClickSubmit process - isVisibleTestSubmitWarningDialog = true`() = runTest {
        with(mockTestViewModel) {
            // given
            process(MockTestUiAction.ClickSubmit)

            // when & then
            uiState.test {
                assertTrue(awaitItem().isVisibleTestSubmitWarningDialog)
            }
        }
    }

    @Test
    fun `Action_ClickCancel process - isVisibleTestEndWarningDialog = true`() = runTest {
        with(mockTestViewModel) {
            // given
            process(MockTestUiAction.ClickCancel)
            // when & then
            uiState.test {
                assertTrue(awaitItem().isVisibleTestEndWarningDialog)
            }
        }
    }

    @Test
    fun `Action_ClickConfirmTestSubmitWarningDialog process - isVisibleTestSubmitWarningDialog = false, Effect_MoveToResult 발생`() =
        runTest {
            with(mockTestViewModel) {
                // given
                coEvery { fakeMockTestRepository.getMockTest(1L) } returns ApiResult.Success(fakeTest)
                coEvery { fakeMockTestRepository.submitMockTest(1L, any()) } returns ApiResult.Success(Unit)

                process(MockTestUiAction.ObserveMockTestItem)

                fakeTest.questions.forEach { selectedQuestion ->
                    process(
                        MockTestUiAction.SelectOption(
                            questionID = selectedQuestion.id,
                            option = selectedQuestion.options.first().toGeneralOptionItem()
                        )
                    )
                }

                val expected = fakeTest.questions.associate { it.id to it.options.first() }

                // when
                process(MockTestUiAction.ClickConfirmTestSubmitWarningDialog)

                // then
                uiState.test {
                    with(awaitItem()) {
                        assertEquals(false, isVisibleTestSubmitWarningDialog)
                    }
                }
                effect.test { assertEquals(MockTestUiEffect.MoveToResult, awaitItem()) }
                coVerify { fakeMockTestRepository.submitMockTest(1L, expected) }
            }
        }

    @Test
    fun `Action_ClickDismissTestSubmitWarningDialog process - isVisibleTestSubmitWarningDialog = false`() =
        runTest {
            with(mockTestViewModel) {
                // given
                process(MockTestUiAction.ClickDismissTestSubmitWarningDialog)
                // when & then
                uiState.test { assertEquals(false, awaitItem().isVisibleTestSubmitWarningDialog) }
                effect.test { expectNoEvents() }
            }
        }

    @Test
    fun `Action_ClickConfirmTestEndWarningDialog process - isVisibleTestEndWarningDialog = false, Effect_CancelTest 발생`() =
        runTest {
            with(mockTestViewModel) {
                // given
                process(MockTestUiAction.ClickConfirmTestEndWarningDialog)
                // when & then
                uiState.test { assertEquals(false, awaitItem().isVisibleTestEndWarningDialog) }
                effect.test { assertEquals(MockTestUiEffect.CancelTest, awaitItem()) }
            }
        }

    @Test
    fun `Action_ClickDismissTestEndWarningDialog process - isVisibleTestEndWarningDialog = false`() =
        runTest {
            with(mockTestViewModel) {
                // given
                process(MockTestUiAction.ClickDismissTestEndWarningDialog)
                // when & then
                uiState.test { assertEquals(false, awaitItem().isVisibleTestEndWarningDialog) }
                effect.test { expectNoEvents() }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `타이머가 시작 후 - remainTimeMs는 1초씩 감소`() = runTest {
        // given
        coEvery { fakeMockTestRepository.getMockTest(1L) } returns ApiResult.Success(fakeTest)
        coEvery { fakeMockTestRepository.submitMockTest(1L, any()) } returns ApiResult.Success(Unit)
        
        with(mockTestViewModel) {

            process(MockTestUiAction.ObserveMockTestItem)
            (1..5).forEach { seconds ->
                // when
                advanceTimeBy(1010)
                // then
                uiState.test {
                    with(awaitItem()) {
                        assertEquals(totalTimeLimitMs - 1000 * seconds, remainTimeMs)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `타이머 만료 - Effect_MoveToResult 발생`() = runTest {
        // given
        coEvery { fakeMockTestRepository.getMockTest(1L) } returns ApiResult.Success(fakeTest)
        coEvery { fakeMockTestRepository.submitMockTest(1L, any()) } returns ApiResult.Success(Unit)

        with(mockTestViewModel) {
            process(MockTestUiAction.ObserveMockTestItem)

            fakeTest.questions.dropLast(1).forEach { selectedQuestion ->
                process(
                    MockTestUiAction.SelectOption(
                        questionID = selectedQuestion.id,
                        option = selectedQuestion.options.first().toGeneralOptionItem()
                    )
                )
            }
            val expected = fakeTest.questions.associate {
                it.id to it.options.first()
            } + mapOf(
                fakeTest.questions.last().id to Option(
                    id = 0,
                    content = ""
                )
            )

            // when
            advanceTimeBy(fakeTest.totalTimeLimit.toMilliSecond() + 100)

            // then
            uiState.test { assertEquals(0, awaitItem().remainTimeMs) }
            effect.test { assertEquals(MockTestUiEffect.MoveToResult, awaitItem()) }
            coVerify { fakeMockTestRepository.submitMockTest(1L, expected) }
        }
    }

    companion object {
        val fakeTest = TestModel(
            questions = listOf(
                Question(
                    id = 1,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
                    options = listOf(
                        Option(
                            id = 1,
                            "트랜잭션을 더 작은 단위로 분할"
                        ),
                        Option(
                            id = 2,
                            "트랜잭션의 타임아웃 시간을 늘림"
                        ),
                        Option(
                            id = 3,
                            "모든 데이터를 메모리에 로드"
                        ),
                        Option(
                            id = 4,
                            "트랜잭션의 격리 수준을 낮춤"
                        ),
                    ),
                    timeLimit = 60,
                ),
                Question(
                    id = 2,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
                    options = listOf(
                        Option(
                            id = 5,
                            "트랜잭션을 더 작은 단위로 분할#2"
                        ),
                        Option(
                            id = 6,
                            "트랜잭션의 타임아웃 시간을 늘림#2"
                        ),
                        Option(
                            id = 7,
                            "모든 데이터를 메모리에 로드#2"
                        ),
                        Option(
                            id = 8,
                            "트랜잭션의 격리 수준을 낮춤#2"
                        ),
                    ),
                    timeLimit = 60,
                ),
            ),
            totalTimeLimit = 120 //2분
        )
    }
}

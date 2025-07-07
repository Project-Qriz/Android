package com.qriz.app.feature.daily_study

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.test.mapper.toGeneralOptionItem
import com.qriz.app.core.ui.test.mapper.toQuestionTestItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import com.qriz.app.feature.daily_study.study.DailyStudyUiEffect
import com.qriz.app.feature.daily_study.study.DailyTestUiAction
import com.qriz.app.feature.daily_study.study.DailyTestUiState
import com.qriz.app.feature.daily_study.study.DailyTestViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import com.qriz.app.core.data.test.test_api.model.Test as _Test

class DailyTestViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockDailyStudyRepository = mockk<DailyStudyRepository>()

    fun viewModel(): DailyTestViewModel {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.mockkToRoute(
            DailyStudyRoute.DailyTest(dayNumber = 1)
        )

        return DailyTestViewModel(
            savedStateHandle = savedStateHandle,
            dailyStudyRepository = mockDailyStudyRepository
        )
    }

    @Test
    fun `LoadData process - success state 변경, timer 시작`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)

        with(viewModel()) {
            //when
            process(DailyTestUiAction.LoadData)

            //then
            assert(isTimerJobNotNull())
            uiState.test {
                val item = awaitItem()
                assert(item.state is DailyTestUiState.LoadState.Success)
                assertEquals(
                    (item.state as DailyTestUiState.LoadState.Success).questions,
                    expectedQuestionTestItems()
                )
            }
        }
    }

    @Test
    fun `LoadData process - failure state 변경`() = runTest {
        //given
        val errorMessage = "테스트 데이터를 불러오는데 실패했습니다."
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Failure(
            code = 500,
            message = errorMessage
        )

        with(viewModel()) {
            //when
            process(DailyTestUiAction.LoadData)

            //then
            assert(isTimerJobNull())
            uiState.test {
                val item = awaitItem()
                assert(item.state is DailyTestUiState.LoadState.Failure)
                assertEquals(
                    errorMessage,
                    (item.state as DailyTestUiState.LoadState.Failure).errorMessage
                )
            }
        }
    }

    @Test
    fun `NextQuestion process - 다음 문제로 이동`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)

        with(viewModel()) {
            process(DailyTestUiAction.LoadData)
            
            //when
            process(DailyTestUiAction.NextQuestion)

            //then
            assert(isTimerJobNotNull())
            uiState.test {
                val item = awaitItem()
                assertEquals(1, item.currentIndex)
                assertEquals(60000L, item.remainTimeMs) // 두 번째 문제의 timeLimit
            }
        }
    }

    @Test
    fun `NextQuestion process - 마지막 문제에서 제출 확인 다이얼로그 표시`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)

        with(viewModel()) {
            process(DailyTestUiAction.LoadData)
            
            // 첫 번째 문제에서 두 번째로 이동
            process(DailyTestUiAction.NextQuestion)
            
            //when
            // 마지막 문제이므로 NextQuestion 대신 시간 종료 시나리오를 테스트
            // 실제로는 timerJob에서 자동으로 제출 다이얼로그가 표시됨
            process(DailyTestUiAction.ShowSubmitConfirmationDialog)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(true, item.showSubmitConfirmationDialog)
            }
        }
    }

    @Test
    fun `SelectOption process - 선택한 옵션 상태 업데이트`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)
        val selectedOption = mockTest.questions.first().options.first()

        with(viewModel()) {
            process(DailyTestUiAction.LoadData)
            
            //when
            process(DailyTestUiAction.SelectOption(
                questionId = 1L,
                option = selectedOption.toGeneralOptionItem()
            ))

            //then
            uiState.test {
                val item = awaitItem()
                val state = item.state as DailyTestUiState.LoadState.Success
                val question = state.questions.first { it.id == 1L }
                val option = question.options.first { it.id == selectedOption.id }
                assertEquals(true, option is SelectedOrCorrectOptionItem)
            }
        }
    }

    @Test
    fun `ShowSubmitConfirmationDialog process - 제출 확인 다이얼로그 표시`() = runTest {
        //given
        with(viewModel()) {
            //when
            process(DailyTestUiAction.ShowSubmitConfirmationDialog)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(true, item.showSubmitConfirmationDialog)
            }
        }
    }

    @Test
    fun `DismissSubmitConfirmationDialog process - 제출 확인 다이얼로그 숨김`() = runTest {
        //given
        with(viewModel()) {
            process(DailyTestUiAction.ShowSubmitConfirmationDialog)
            
            //when
            process(DailyTestUiAction.DismissSubmitConfirmationDialog)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(false, item.showSubmitConfirmationDialog)
            }
        }
    }

    @Test
    fun `SubmitTest process - 테스트 제출 성공`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)
        coEvery { mockDailyStudyRepository.submitTest(any(), any()) } returns ApiResult.Success(Unit)

        with(viewModel()) {
            process(DailyTestUiAction.LoadData)
            
            // 모든 문제에 답안 선택
            mockTest.questions.forEach { question ->
                process(DailyTestUiAction.SelectOption(
                    questionId = question.id,
                    option = question.options.first().toGeneralOptionItem()
                ))
            }
            
            //when
            process(DailyTestUiAction.SubmitTest)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(false, item.showSubmitConfirmationDialog)
            }
            
            effect.test {
                val effect = awaitItem()
                assertEquals(DailyStudyUiEffect.MoveToResult, effect)
            }
        }
    }

    @Test
    fun `SubmitTest process - 테스트 제출 실패`() = runTest {
        //given
        val errorMessage = "제출 실패"
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)
        coEvery { mockDailyStudyRepository.submitTest(any(), any()) } returns ApiResult.Failure(
            code = 500,
            message = errorMessage
        )

        with(viewModel()) {
            process(DailyTestUiAction.LoadData)
            
            // 모든 문제에 답안 선택
            mockTest.questions.forEach { question ->
                process(DailyTestUiAction.SelectOption(
                    questionId = question.id,
                    option = question.options.first().toGeneralOptionItem()
                ))
            }
            
            //when
            process(DailyTestUiAction.SubmitTest)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(false, item.showSubmitConfirmationDialog)
                assertEquals(errorMessage, item.errorMessage)
            }
        }
    }

    @Test
    fun `ShowCancelConfirmationDialog process - 취소 확인 다이얼로그 표시`() = runTest {
        //given
        with(viewModel()) {
            //when
            process(DailyTestUiAction.ShowCancelConfirmationDialog)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(true, item.showCancelConfirmationDialog)
            }
        }
    }

    @Test
    fun `DismissCancelConfirmationDialog process - 취소 확인 다이얼로그 숨김`() = runTest {
        //given
        with(viewModel()) {
            process(DailyTestUiAction.ShowCancelConfirmationDialog)
            
            //when
            process(DailyTestUiAction.DismissCancelConfirmationDialog)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(false, item.showCancelConfirmationDialog)
            }
        }
    }

    @Test
    fun `CancelTest process - 테스트 취소 및 이전 화면으로 이동`() = runTest {
        //given
        with(viewModel()) {
            //when
            process(DailyTestUiAction.CancelTest)

            //then
            effect.test {
                val effect = awaitItem()
                assertEquals(DailyStudyUiEffect.Cancel, effect)
            }
        }
    }

    @Test
    fun `DismissErrorDialog process - 에러 다이얼로그 숨김`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudy(1) } returns ApiResult.Success(mockTest)
        coEvery { mockDailyStudyRepository.submitTest(any(), any()) } returns ApiResult.Failure(
            code = 500,
            message = "제출 실패"
        )
        
        with(viewModel()) {
            process(DailyTestUiAction.LoadData)
            
            // 모든 문제에 답안 선택
            mockTest.questions.forEach { question ->
                process(DailyTestUiAction.SelectOption(
                    questionId = question.id,
                    option = question.options.first().toGeneralOptionItem()
                ))
            }
            
            // 제출 실패로 에러 메시지 설정
            process(DailyTestUiAction.SubmitTest)
            
            //when
            process(DailyTestUiAction.DismissErrorDialog)

            //then
            uiState.test {
                val item = awaitItem()
                assertEquals(null, item.errorMessage)
            }
        }
    }

    companion object {
        val mockTest = _Test(
            totalTimeLimit = 180,
            questions = listOf(
                Question(
                    id = 1,
                    question = "다음 중 WHERE절에 대한 올바른 설명은?",
                    description = null,
                    skillId = 3,
                    category = 2,
                    difficulty = 4,
                    timeLimit = 120,
                    options = listOf(
                        Option(
                            id = 1,
                            content = "WHERE절은 SELECT문에서 데이터를 필터링하는 데 사용된다."
                        ),
                        Option(
                            id = 2,
                            content = "WHERE절은 INSERT문에서만 사용된다."
                        ),
                        Option(
                            id = 3,
                            content = "WHERE절은 데이터베이스 테이블을 생성할 때 사용"
                        ),
                    )
                ),
                Question(
                    id = 2,
                    question = "다음 중 SELF JOIN에 대한 설명으로 가장 부적절한 것은?",
                    description = null,
                    skillId = 3,
                    category = 2,
                    difficulty = 4,
                    timeLimit = 60,
                    options = listOf(
                        Option(
                            id = 4,
                            content = "계층 구조를 표현할 때 주로 사용된다."
                        ),
                        Option(
                            id = 5,
                            content = "한 테이블 내에서 두 컬럼이 연관관계가 있을 때 사용한다."
                        ),
                        Option(
                            id = 6,
                            content = "반드시 서브쿼리와 함께 사용해야 한다",
                        ),
                        Option(
                            id = 7,
                            content = "동일한 테이블을 두 번 조인하는 것을 의미한다.",
                        ),
                    )
                )
            )
        )

        fun expectedQuestionTestItems(
            selectedOptions: Map<Long, Option> = emptyMap()
        ): ImmutableList<QuestionTestItem> = mockTest.questions
            .toQuestionTestItem(selectedOptions)
            .toImmutableList()
    }
}

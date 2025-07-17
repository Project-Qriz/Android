package com.qriz.app.feature.daily_study

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.core.data.daily_study.daily_study_api.model.CategoryResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.ConceptResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyTestResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.SubjectResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyReviewResult
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.daily_study.result.DailyTestResultUiAction
import com.qriz.app.feature.daily_study.result.DailyTestResultUiEffect
import com.qriz.app.feature.daily_study.result.DailyTestResultUiState
import com.qriz.app.feature.daily_study.result.DailyTestResultViewModel
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DailyTestResultViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockUserRepository = mockk<UserRepository>()
    private val mockDailyStudyRepository = mockk<DailyStudyRepository>()
    private val mockConceptBookRepository = mockk<ConceptBookRepository>()

    private fun viewModel(dayNumber: Int = 1): DailyTestResultViewModel {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.mockkToRoute(
            DailyStudyRoute.DailyTestResult(
                dayNumber = dayNumber,
            ),
        )

        return DailyTestResultViewModel(
            savedStateHandle = savedStateHandle,
            userRepository = mockUserRepository,
            dailyStudyRepository = mockDailyStudyRepository,
            conceptBookRepository = mockConceptBookRepository,
        )
    }

    private val mockUser = User(
        userId = "test123",
        name = "테스트 사용자",
        email = "test@example.com",
        previewTestStatus = PreviewTestStatus.PREVIEW_COMPLETED
    )

    private val mockDailyTestResult = DailyTestResult(
        passed = true,
        isReview = false,
        isComprehensiveReview = false,
        totalScore = 85,
        skillItems = emptyList(),
        questionResults = emptyList()
    )

    private val mockWeeklyReviewResult = WeeklyReviewResult(
        totalScore = 95,
        subjectItems = listOf(
            SubjectResult(
                subjectName = "1과목",
                score = 35,
                categoryItems = listOf(
                    CategoryResult(
                        categoryName = "데이터 모델링의 이해",
                        score = 35,
                        conceptItems = listOf(
                            ConceptResult(
                                conceptName = "엔터티",
                                score = 15
                            ),
                            ConceptResult(
                                conceptName = "관계",
                                score = 20
                            )
                        )
                    )
                )
            ),
            SubjectResult(
                subjectName = "2과목",
                score = 60,
                categoryItems = listOf(
                    CategoryResult(
                        categoryName = "SQL 기본",
                        score = 60,
                        conceptItems = listOf(
                            ConceptResult(
                                conceptName = "SELECT 문",
                                score = 25
                            ),
                            ConceptResult(
                                conceptName = "조인",
                                score = 35
                            )
                        )
                    )
                )
            )
        )
    )

    @Test
    fun `LoadData - 성공 시 상태 업데이트`() = runTest {
        // given
        coEvery { mockUserRepository.getUser() } returns ApiResult.Success(mockUser)
        coEvery { mockConceptBookRepository.getData() } returns emptyList<Subject>()
        coEvery { mockDailyStudyRepository.getDailyTestResult(1) } returns ApiResult.Success(mockDailyTestResult)

        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.LoadData)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.state is DailyTestResultUiState.LoadState.Success)
            assertEquals(
                "테스트 사용자",
                state.userName
            )
            assertFalse(state.isReview)
            assertFalse(state.isComprehensiveReview)
        }
    }

    @Test
    fun `LoadData - 실패 시 에러 상태 업데이트`() = runTest {
        // given
        val errorMessage = "API 오류"
        coEvery { mockUserRepository.getUser() } returns ApiResult.Success(mockUser)
        coEvery { mockConceptBookRepository.getData() } returns emptyList<Subject>()
        coEvery { mockDailyStudyRepository.getDailyTestResult(1) } returns ApiResult.Failure(
            code = -1,
            message = errorMessage
        )

        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.LoadData)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.state is DailyTestResultUiState.LoadState.Failure)
            assertEquals(
                errorMessage,
                (state.state as DailyTestResultUiState.LoadState.Failure).message
            )
            assertFalse((state.state as DailyTestResultUiState.LoadState.Failure).canRetry)
        }
    }

    @Test
    fun `LoadData - 네트워크 오류 시 재시도 가능한 에러 상태 업데이트`() = runTest {
        // given
        coEvery { mockUserRepository.getUser() } returns ApiResult.Success(mockUser)
        coEvery { mockConceptBookRepository.getData() } returns emptyList<Subject>()
        coEvery { mockDailyStudyRepository.getDailyTestResult(1) } returns ApiResult.NetworkError(IOException())

        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.LoadData)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.state is DailyTestResultUiState.LoadState.Failure)
            assertEquals(
                NETWORK_IS_UNSTABLE,
                (state.state as DailyTestResultUiState.LoadState.Failure).message
            )
            assertTrue((state.state as DailyTestResultUiState.LoadState.Failure).canRetry)
        }
    }

    @Test
    fun `LoadData - 알 수 없는 오류 시 재시도 가능한 에러 상태 업데이트`() = runTest {
        // given
        coEvery { mockUserRepository.getUser() } returns ApiResult.Success(mockUser)
        coEvery { mockConceptBookRepository.getData() } returns emptyList<Subject>()
        coEvery { mockDailyStudyRepository.getDailyTestResult(1) } returns ApiResult.UnknownError(Exception())

        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.LoadData)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.state is DailyTestResultUiState.LoadState.Failure)
            assertEquals(
                UNKNOWN_ERROR,
                (state.state as DailyTestResultUiState.LoadState.Failure).message
            )
            assertTrue((state.state as DailyTestResultUiState.LoadState.Failure).canRetry)
        }
    }

    @Test
    fun `ClickFilter - 필터 드롭다운 상태 토글`() = runTest {
        // given
        val viewModel = viewModel()
        val initialState = viewModel.uiState.value
        assertFalse(initialState.showFilterDropDown)

        // when
        viewModel.process(DailyTestResultUiAction.ClickFilter)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.showFilterDropDown)
        }
    }

    @Test
    fun `ShowDetail - 상세보기 뷰 타입 변경 및 주간 리뷰 데이터 로드 성공`() = runTest {
        // given
        coEvery { mockDailyStudyRepository.getWeeklyReviewResult(1) } returns ApiResult.Success(mockWeeklyReviewResult)
        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.ShowDetail)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(
                DailyTestResultUiState.ViewType.DETAIL,
                state.viewType
            )
            assertTrue(state.weeklyReviewState is DailyTestResultUiState.WeeklyReviewState.Success)

            val successState =
                state.weeklyReviewState as DailyTestResultUiState.WeeklyReviewState.Success

            assertEquals(mockWeeklyReviewResult, successState.data)
        }
    }

    @Test
    fun `ShowDetail - 상세보기 뷰 타입 변경 및 주간 리뷰 데이터 로드 실패`() = runTest {
        // given
        val errorMessage = "주간 리뷰 데이터 로드 실패"
        coEvery { mockDailyStudyRepository.getWeeklyReviewResult(1) } returns ApiResult.Failure(
            code = -1,
            message = errorMessage
        )
        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.ShowDetail)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(
                DailyTestResultUiState.ViewType.DETAIL,
                state.viewType
            )
            assertTrue(state.weeklyReviewState is DailyTestResultUiState.WeeklyReviewState.Failure)
            assertEquals(
                errorMessage,
                (state.weeklyReviewState as DailyTestResultUiState.WeeklyReviewState.Failure).message
            )
        }
    }

    @Test
    fun `ShowDetail - 네트워크 오류 시 실패 상태 업데이트`() = runTest {
        // given
        coEvery { mockDailyStudyRepository.getWeeklyReviewResult(1) } returns ApiResult.NetworkError(IOException())
        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.ShowDetail)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(
                DailyTestResultUiState.ViewType.DETAIL,
                state.viewType
            )
            assertTrue(state.weeklyReviewState is DailyTestResultUiState.WeeklyReviewState.Failure)
            assertEquals(
                NETWORK_IS_UNSTABLE,
                (state.weeklyReviewState as DailyTestResultUiState.WeeklyReviewState.Failure).message
            )
        }
    }

    @Test
    fun `ClickBackButton - DETAIL 뷰에서 TOTAL 뷰로 변경`() = runTest {
        // given
        val viewModel = viewModel()
        coEvery { mockDailyStudyRepository.getWeeklyReviewResult(day = 1) } returns ApiResult.Success(mockWeeklyReviewResult)
        viewModel.process(DailyTestResultUiAction.ShowDetail) // DETAIL 뷰로 변경

        // when
        viewModel.process(DailyTestResultUiAction.ClickBackButton)

        // then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(
                DailyTestResultUiState.ViewType.TOTAL,
                state.viewType
            )
        }
    }

    @Test
    fun `ClickBackButton - TOTAL 뷰에서 Close 이펙트 발생`() = runTest {
        // given
        val viewModel = viewModel()

        // when
        viewModel.process(DailyTestResultUiAction.ClickBackButton)

        // then
        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(
                DailyTestResultUiEffect.Close,
                effect
            )
        }
    }
}

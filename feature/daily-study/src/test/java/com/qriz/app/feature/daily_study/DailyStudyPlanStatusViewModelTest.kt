package com.qriz.app.feature.daily_study

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlanDetail
import com.qriz.app.core.data.daily_study.daily_study_api.model.SimplePlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.DailyStudyRoute
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.feature.daily_study.status.DailyStudyPlanStatusUiAction
import com.qriz.app.feature.daily_study.status.DailyStudyPlanStatusViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

inline fun <reified T : Any> SavedStateHandle.mockkToRoute(page: T) {
    mockkStatic("androidx.navigation.SavedStateHandleKt")
    every { toRoute<T>() } returns page
}

class DailyStudyPlanStatusViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockDailyStudyRepository = mockk<DailyStudyRepository>()

    private fun viewModel(
        dayNumber: Int = 1,
        isReview: Boolean = false,
        isComprehensiveReview: Boolean = false,
    ): DailyStudyPlanStatusViewModel {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.mockkToRoute(
            DailyStudyRoute.DailyStudyPlanStatus(
                dayNumber = dayNumber,
                isReview = isReview,
                isComprehensiveReview = isComprehensiveReview,
            ),
        )

        return DailyStudyPlanStatusViewModel(
            savedStateHandle = savedStateHandle,
            dailyStudyRepository = mockDailyStudyRepository
        )
    }

    private val mockSkills = listOf(
        SimplePlannedSkill(
            id = 1,
            keyConcept = "SQL 기본",
            description = "SELECT문과 WHERE절 사용법"
        ),
        SimplePlannedSkill(
            id = 2,
            keyConcept = "JOIN",
            description = "INNER JOIN과 LEFT JOIN 차이점"
        )
    )

    @Test
    fun `savedStateHandle 값에 따른 초기 설정`() = runTest {
        with(viewModel()) {
            val state = uiState.value
            assertFalse(state.isReview)
            assertFalse(state.isComprehensiveReview)
            assertEquals(
                state.skillTitleResId,
                R.string.daily_study
            )
        }

        with(viewModel(isReview = true)) {
            val state = uiState.value
            assertTrue(state.isReview)
            assertFalse(state.isComprehensiveReview)
            assertEquals(
                state.skillTitleResId,
                R.string.weekly_review_skills
            )
        }

        with(
            viewModel(
                isReview = true,
                isComprehensiveReview = true
            )
        ) {
            val state = uiState.value
            assertTrue(state.isReview)
            assertTrue(state.isComprehensiveReview)
            assertEquals(
                state.skillTitleResId,
                R.string.comprehensive_review_skills
            )
        }
    }

    @Test
    fun `LoadData process success - isLoading false, errorMessage null, 상태 변경`() = runTest {
        //given
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 1,
            passed = true,
            retestEligible = false,
            totalScore = 85.5,
            available = false
        )
        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Success(mockDetail)

        with(viewModel()) {
            //when
            process(DailyStudyPlanStatusUiAction.LoadData)

            //then
            uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertNull(state.errorMessage)
                assertTrue(state.passed)
                assertEquals(
                    85.5,
                    state.score
                )
                assertEquals(
                    mockSkills.toImmutableList(),
                    state.skills
                )
                assertFalse(state.available)
                assertFalse(state.canRetry)
                assertEquals(
                    1,
                    state.attemptCount
                )
            }
        }
    }

    @Test
    fun `LoadData process failure - isLoading false, errorMessage 변경`() = runTest {
        //given
        val fakeErrorMessage = "API 오류"
        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Failure(
            code = -1,
            message = fakeErrorMessage,
        )

        with(viewModel()) {
            //when
            process(DailyStudyPlanStatusUiAction.LoadData)

            //then
            uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(fakeErrorMessage, state.errorMessage)
            }
        }
    }

    @Test
    fun `LoadData process networkError - isLoading false, errorMessage 변경`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.NetworkError(IOException())

        with(viewModel()) {
            //when
            process(DailyStudyPlanStatusUiAction.LoadData)

            //then
            uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(NETWORK_IS_UNSTABLE, state.errorMessage)
            }
        }
    }

    @Test
    fun `LoadData process unknownError - isLoading false, errorMessage 변경`() = runTest {
        //given
        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.UnknownError(Exception())

        with(viewModel()) {
            //when
            process(DailyStudyPlanStatusUiAction.LoadData)

            //then
            uiState.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(UNKNOWN_ERROR, state.errorMessage)
            }
        }
    }

    @Test
    fun `ShowRetryConfirmDialog process - 상태변경`() = runTest {
        //given
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 1,
            passed = true,
            retestEligible = false,
            totalScore = 85.5,
            available = false
        )


    }

    @Test
    fun `LoadData 성공 시 상태가 올바르게 업데이트되는지 확인 - 테스트 불가능 케이스`() = runTest {
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 0,
            passed = false,
            retestEligible = false,
            totalScore = 0.0,
            available = false
        )

        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Success(mockDetail)

        val viewModel = viewModel()
        viewModel.process(DailyStudyPlanStatusUiAction.LoadData)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.passed)
        assertEquals(
            0.0,
            state.score
        )
        assertEquals(
            mockSkills.toImmutableList(),
            state.skills
        )
        assertFalse(state.available)
        assertFalse(state.canRetry)
        assertEquals(
            0,
            state.attemptCount
        )
    }

    @Test
    fun `LoadData 성공 시 상태가 올바르게 업데이트되는지 확인 - 최대 시도 횟수 도달 케이스`() = runTest {
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 2,
            passed = false,
            retestEligible = false,
            totalScore = 45.0,
            available = true
        )

        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Success(mockDetail)

        val viewModel = viewModel()
        viewModel.process(DailyStudyPlanStatusUiAction.LoadData)

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.passed)
        assertEquals(
            45.0,
            state.score
        )
        assertEquals(
            mockSkills.toImmutableList(),
            state.skills
        )
        assertTrue(state.available)
        assertFalse(state.canRetry)
        assertEquals(
            2,
            state.attemptCount
        )
        assertTrue(state.isComplete) // attemptCount가 2이므로 완료 상태
    }

    @Test
    fun `UiState의 계산된 속성들이 올바르게 동작하는지 확인 - 테스트 불가능 상태`() = runTest {
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 0,
            passed = false,
            retestEligible = false,
            totalScore = 0.0,
            available = false
        )

        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Success(mockDetail)

        val viewModel = viewModel()
        viewModel.process(DailyStudyPlanStatusUiAction.LoadData)

        val state = viewModel.uiState.value
        assertFalse(state.isComplete)
        assertFalse(state.available)
    }

    @Test
    fun `UiState의 계산된 속성들이 올바르게 동작하는지 확인 - 테스트 완료 상태 (통과)`() = runTest {
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 1,
            passed = true,
            retestEligible = false,
            totalScore = 85.5,
            available = true
        )

        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Success(mockDetail)

        val viewModel = viewModel()
        viewModel.process(DailyStudyPlanStatusUiAction.LoadData)

        val state = viewModel.uiState.value
        assertTrue(state.isComplete) // passed = true이므로 완료
        assertTrue(state.available)
    }

    @Test
    fun `UiState의 계산된 속성들이 올바르게 동작하는지 확인 - 테스트 완료 상태 (최대 시도)`() = runTest {
        val mockDetail = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = mockSkills,
            attemptCount = 2,
            passed = false,
            retestEligible = false,
            totalScore = 45.0,
            available = true
        )

        coEvery { mockDailyStudyRepository.getDailyStudyPlanDetail(1) } returns ApiResult.Success(mockDetail)

        val viewModel = viewModel()
        viewModel.process(DailyStudyPlanStatusUiAction.LoadData)

        val state = viewModel.uiState.value
        assertTrue(state.isComplete) // attemptCount = 2이므로 완료
        assertTrue(state.available)
    }
}

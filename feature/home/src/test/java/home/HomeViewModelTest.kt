package home

import app.cash.turbine.test
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.ImportanceLevel
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.feature.home.HomeUiAction
import com.qriz.app.feature.home.HomeUiEffect
import com.qriz.app.feature.home.HomeUiState
import com.qriz.app.feature.home.HomeViewModel
import com.qriz.app.feature.home.R
import com.qriz.app.feature.home.component.UserExamUiState
import com.qriz.app.core.ui.common.const.ExamScheduleState
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HomeViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val userRepository = mockk<UserRepository>()
    private val examRepository = mockk<ExamRepository>()
    private val dailyStudyRepository = mockk<DailyStudyRepository>()

    private fun homeViewModel() = HomeViewModel(
        userRepository = userRepository,
        examRepository = examRepository,
        dailyStudyRepository = dailyStudyRepository,
    )

    @Test
    fun `dataFlow - 유효한 유저 시험일정이 있을 때 userExamState Scheduled`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)

        //then
        with(viewModel) {
            uiState.test {
                val state = awaitItem()
                assertEquals(
                    UserExamUiState.Scheduled(
                        examName = USER_EXAM_NAME,
                        examPeriod = USER_EXAM_PERIOD,
                        examDate = USER_EXAM_DATE,
                        dday = 10,
                        ddayType = DdayType.BEFORE
                    ),
                    state.userExamState
                )
            }
        }
    }

    @Test
    fun `dataFlow - 유효한 유저 시험일정이 없을 때 userExamState NotScheduled`() = runTest {
        //given
        mockUserFlow()
        mockUserExamFailure()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)

        //then
        with(viewModel) {
            uiState.test {
                assertEquals(
                    UserExamUiState.NoSchedule,
                    awaitItem().userExamState
                )
            }
        }
    }

    @Test
    fun `dataFlow - 이미 지난 시험일정을 가지고 있을 때 userExamState PastExam`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess(dday = 10, ddayType = DdayType.AFTER)
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)

        //then
        with(viewModel) {
            uiState.test {
                assertEquals(
                    UserExamUiState.PastExam,
                    expectMostRecentItem().userExamState
                )
            }
        }
    }
    
    @Test
    fun `dataFlow - 프리뷰 테스트를 완료한 경우 DailyStudyPlan, WeeklyRecommendation을 불러온다`() = runTest {
        //given
        mockUserFlow(PreviewTestStatus.PREVIEW_COMPLETED)
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)

        //then
        with(viewModel) {
            uiState.test {
                with(awaitItem()) {
                    dailyStudyPlans shouldBe dailyStudyPlans
                    weeklyRecommendations shouldBe weeklyRecommendations
                }
            }
        }
    }

    @Test
    fun `ClickApply process - BottomSheet state 변경, scheduleState 변경`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesSuccess()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)

        //then
        with(viewModel) {
            process(HomeUiAction.ClickApply)

            uiState.test {
                //Loading
                assertExamSchedulesLoadingState(awaitItem())

                //Success
                val success = awaitItem()
                assertEquals(
                    ExamScheduleState.Success(examSchedulesApplied),
                    success.schedulesState,
                )
                assertEquals(
                    10,
                    success.userApplicationId,
                )
            }
        }
    }

    @Test
    fun `ClickApply process - API 실패 시 scheduleState Failure`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesFailure()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)
        testScheduler.advanceUntilIdle()
        viewModel.process(HomeUiAction.ClickApply)

        //then
        with(viewModel) {
            uiState.test {
                val state = expectMostRecentItem()
                assertEquals(
                    ExamScheduleState.Error("Error"),
                    state.schedulesState
                )
                assertEquals("Error", state.examSchedulesErrorMessage)
                assertEquals(false, state.isShowExamScheduleBottomSheet)
            }
        }
    }

    @Test
    fun `ClickApply process - 네트워크 에러 시 scheduleState Failure`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesNetworkError()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)
        testScheduler.advanceUntilIdle()
        viewModel.process(HomeUiAction.ClickApply)

        //then
        with(viewModel) {
            uiState.test {
                val state = expectMostRecentItem()
                assertEquals(
                    ExamScheduleState.Error(NETWORK_IS_UNSTABLE),
                    state.schedulesState
                )
                assertEquals(NETWORK_IS_UNSTABLE, state.examSchedulesErrorMessage)
                assertEquals(false, state.isShowExamScheduleBottomSheet)
            }
        }
    }

    @Test
    fun `ClickApply process - 알 수 없는 에러 시 scheduleState Failure`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesUnknownError()
        val viewModel = homeViewModel()

        //when
        viewModel.process(HomeUiAction.ObserveClient)
        testScheduler.advanceUntilIdle()
        viewModel.process(HomeUiAction.ClickApply)

        //then
        with(viewModel) {
            uiState.test {
                val state = expectMostRecentItem()
                assertEquals(
                    ExamScheduleState.Error("알 수 없는 오류가 발생했습니다."),
                    state.schedulesState
                )
                assertEquals("알 수 없는 오류가 발생했습니다.", state.examSchedulesErrorMessage)
                assertEquals(false, state.isShowExamScheduleBottomSheet)
            }
        }
    }

    @Test
    fun `ClickExamSchedule_NoApplied_Sucess - apply() 호출, Snackbar 노출, isShowExamScheduleBottomSheet false`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesSuccess(isApplied = false)
        mockApplyExamSuccess()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        testScheduler.advanceUntilIdle()
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        coVerify { examRepository.applyExam(examId = 1) }
        viewModel.uiState.test {
            with(awaitItem()) {
                assertTrue(isShowExamScheduleBottomSheet.not())
            }
        }
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(defaultResId = R.string.success_to_apply_exam),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_Applied_Sucess - apply() 호출, Snackbar 노출, isShowExamScheduleBottomSheet false`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesSuccess()
        mockApplyEditExamSuccess()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        coVerify { examRepository.editExam(uaid = 10, examId = 1) }
        viewModel.uiState.test {
            assertTrue(awaitItem().isShowExamScheduleBottomSheet.not())
        }

        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(defaultResId = R.string.success_to_apply_exam),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_NoApplied_ApplyFailure - 실패 스낵바 노출`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockExamSchedulesSuccess(isApplied = false)
        mockApplyExamFailure()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(message = "Apply Error"),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_NoApplied_NetworkError - 네트워크 에러 스낵바 노출`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockExamSchedulesSuccess(isApplied = false)
        mockApplyExamNetworkError()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(message = NETWORK_IS_UNSTABLE),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_NoApplied_UnknownError - 알 수 없는 에러 스낵바 노출`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockExamSchedulesSuccess(isApplied = false)
        mockApplyExamUnknownError()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(message = "알 수 없는 오류가 발생했습니다."),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_Applied_Failure - 실패 스낵바 노출`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesSuccess()
        mockEditExamFailure()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(message = "Edit Error"),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_Applied_NetworkError - 네트워크 에러 스낵바 노출`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesSuccess()
        mockEditExamNetworkError()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(message = NETWORK_IS_UNSTABLE),
                awaitItem(),
            )
        }
    }

    @Test
    fun `ClickExamSchedule_Applied_UnknownError - 알 수 없는 에러 스낵바 노출`() = runTest {
        //given
        mockUserFlow()
        mockUserExamSuccess()
        mockDailyStudyPlanSuccess()
        mockWeeklyRecommendation()
        mockExamSchedulesSuccess()
        mockEditExamUnknownError()

        val viewModel = homeViewModel()
        viewModel.process(HomeUiAction.ObserveClient)
        viewModel.process(HomeUiAction.LoadToExamSchedules)
        testScheduler.advanceUntilIdle()

        //when
        viewModel.process(HomeUiAction.ClickExamSchedule(examId = 1))

        //then
        viewModel.effect.test {
            assertEquals(
                HomeUiEffect.ShowSnackBar(message = "알 수 없는 오류가 발생했습니다."),
                awaitItem(),
            )
        }
    }

    private fun mockUserExamSuccess(dday: Int = 10, ddayType: DdayType = DdayType.BEFORE) {
        coEvery { examRepository.getUserExams() } returns flowOf(
            ApiResult.Success(
                UserExam(
                    examName = USER_EXAM_NAME,
                    period = USER_EXAM_PERIOD,
                    examDate = USER_EXAM_DATE,
                    dday = dday,
                    ddayType = ddayType,
                )
            )
        )
    }

    private fun mockUserFlow(previewTestStatus: PreviewTestStatus = PreviewTestStatus.PREVIEW_COMPLETED) {
        coEvery { userRepository.getUserFlow() } returns flowOf(
            User(
                userId = "qriz",
                name = "홍길동",
                email = "qriz@qriz.com",
                previewTestStatus = previewTestStatus
            )
        )
    }

    private fun mockUserExamFailure() {
        coEvery { examRepository.getUserExams() } returns flowOf(
            ApiResult.Failure(
                code = -1,
                message = "Error"
            )
        )
    }

    private fun mockUserExamNetworkError() {
        coEvery { examRepository.getUserExams() } returns flowOf(
            ApiResult.NetworkError(exception = IOException())
        )
    }

    private fun mockUserExamUnknownError() {
        coEvery { examRepository.getUserExams() } returns flowOf(
            ApiResult.UnknownError(
                throwable = Exception("Unknown Error"),
            )
        )
    }

    private fun mockExamSchedulesSuccess(isApplied: Boolean = true) {
        coEvery { examRepository.getExamSchedules() } coAnswers {
            delay(100)
            ApiResult.Success(data = if(isApplied) examSchedulesApplied else examSchedulesNotApplied)
        }
    }

    private fun mockExamSchedulesFailure() {
        coEvery { examRepository.getExamSchedules() } returns ApiResult.Failure(
            code = -1,
            message = "Error"
        )
    }

    private fun mockExamSchedulesNetworkError() {
        coEvery { examRepository.getExamSchedules() } returns ApiResult.NetworkError(
            exception = IOException()
        )
    }

    private fun mockExamSchedulesUnknownError() {
        coEvery { examRepository.getExamSchedules() } returns ApiResult.UnknownError(
            throwable = Exception("Unknown Error")
        )
    }

    private fun mockApplyExamSuccess() {
        coEvery { examRepository.applyExam(any()) } returns ApiResult.Success(Unit)
    }

    private fun mockApplyExamFailure() {
        coEvery { examRepository.applyExam(any()) } returns ApiResult.Failure(
            code = -1,
            message = "Apply Error"
        )
    }

    private fun mockApplyExamNetworkError() {
        coEvery { examRepository.applyExam(any()) } returns ApiResult.NetworkError(
            exception = IOException()
        )
    }

    private fun mockApplyExamUnknownError() {
        coEvery { examRepository.applyExam(any()) } returns ApiResult.UnknownError(
            throwable = Exception("Apply Unknown Error")
        )
    }

    private fun mockApplyEditExamSuccess() {
        coEvery { examRepository.editExam(any(), any()) } returns ApiResult.Success(Unit)
    }

    private fun mockEditExamFailure() {
        coEvery { examRepository.editExam(any(), any()) } returns ApiResult.Failure(
            code = -1,
            message = "Edit Error"
        )
    }

    private fun mockEditExamNetworkError() {
        coEvery { examRepository.editExam(any(), any()) } returns ApiResult.NetworkError(
            exception = IOException()
        )
    }

    private fun mockEditExamUnknownError() {
        coEvery { examRepository.editExam(any(), any()) } returns ApiResult.UnknownError(
            throwable = Exception("Edit Unknown Error")
        )
    }

    private fun mockDailyStudyPlanSuccess() {
        coEvery { dailyStudyRepository.getDailyStudyPlanFlow() } returns
                flowOf(ApiResult.Success(dailyStudyPlans))
    }

    private fun mockWeeklyRecommendation() {
        coEvery { dailyStudyRepository.getWeeklyRecommendation() } returns
                flowOf(ApiResult.Success(weeklyRecommendations))
    }

    private fun assertExamSchedulesLoadingState(state: HomeUiState) {
        print("${state.schedulesState} // ${state.isShowExamScheduleBottomSheet} // ${state.examSchedulesErrorMessage}")
        assertEquals(
            ExamScheduleState.Loading,
            state.schedulesState
        )
        assertTrue(state.isShowExamScheduleBottomSheet)
        assertNull(state.examSchedulesErrorMessage)
    }

    companion object {
        const val USER_EXAM_NAME = "Test Exam"
        const val USER_EXAM_DATE = "8월 23일(토)"
        const val USER_EXAM_PERIOD = "07.21(월) 10:00 ~ 07.25(금) 18:00"

        const val EXPIRED_USER_EXAM_NAME = "Expired Exam"
        const val EXPIRED_USER_EXAM_DATE = "4월 14일(토)"
        const val EXPIRED_USER_EXAM_PERIOD = "04.11(월) 10:00 ~ 04.13(금) 18:00"

        val examSchedulesNotApplied = persistentListOf(
            Schedule(
                applicationId = 1,
                userApplyId = null,
                examName = "SQLD1",
                period = "07.21(월) 10:00 ~ 07.25(금) 18:00",
                examDate = "8월 23일(토)",
                releaseDate = "8월 29일",
                periodStart = LocalDateTime.of(
                    2025,
                    Month.JULY,
                    21,
                    10,
                    0,
                    0,
                    0,
                ),
                periodEnd = LocalDateTime.of(
                    2025,
                    Month.JULY,
                    25,
                    18,
                    0,
                    0,
                    0,
                ),
            ),
            Schedule(
                applicationId = 2,
                userApplyId = null,
                examName = "SQLD2",
                period = "09.26(월) 10:00 ~ 09.30(금) 18:00",
                examDate = "10월 15일(토)",
                releaseDate = "10월 29일",
                periodStart = LocalDateTime.of(
                    2025,
                    Month.SEPTEMBER,
                    26,
                    10,
                    0,
                    0,
                    0,
                ),
                periodEnd = LocalDateTime.of(
                    2025,
                    Month.SEPTEMBER,
                    30,
                    18,
                    0,
                    0,
                    0,
                ),
            )
        )

        val examSchedulesApplied = persistentListOf(
            Schedule(
                applicationId = 1,
                userApplyId = 10,
                examName = "SQLD1",
                period = "07.21(월) 10:00 ~ 07.25(금) 18:00",
                examDate = "8월 23일(토)",
                releaseDate = "8월 29일",
                periodStart = LocalDateTime.of(
                    2025,
                    Month.JULY,
                    21,
                    10,
                    0,
                    0,
                    0,
                ),
                periodEnd = LocalDateTime.of(
                    2025,
                    Month.JULY,
                    25,
                    18,
                    0,
                    0,
                    0,
                ),
            ),
            Schedule(
                applicationId = 2,
                userApplyId = null,
                examName = "SQLD2",
                period = "09.26(월) 10:00 ~ 09.30(금) 18:00",
                examDate = "10월 15일(토)",
                releaseDate = "10월 29일",
                periodStart = LocalDateTime.of(
                    2025,
                    Month.SEPTEMBER,
                    26,
                    10,
                    0,
                    0,
                    0,
                ),
                periodEnd = LocalDateTime.of(
                    2025,
                    Month.SEPTEMBER,
                    30,
                    18,
                    0,
                    0,
                    0,
                ),
            )
        )

        private val dailyStudyPlans = listOf(
            DailyStudyPlan(
                id = 1,
                planDate = LocalDate.parse("2025-07-01"),
                completionDate = null,
                reviewDay = false,
                comprehensiveReviewDay = false,
                completed = false,
                plannedSkills = listOf(
                    PlannedSkill(
                        id = 1,
                        type = "SQL기본",
                        keyConcept = "WHERE절",
                        description = ""
                    )
                )
            )
        )

        private val weeklyRecommendations = listOf(
            WeeklyRecommendation(
                skillId = 1,
                keyConcepts = "WHERE절",
                description = "",
                importanceLevel = ImportanceLevel.HIGH,
                frequency = 2,
                incorrectRate = 1.1,
            )
        )
    }
}

package com.qriz.core.data.application

import app.cash.turbine.test
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.application.api.ApplicationApi
import com.qriz.app.core.network.application.model.ExamListResponse
import com.qriz.app.core.network.application.model.ExamSchedule
import com.qriz.app.core.network.application.model.request.ApplicationModifyRequest
import com.qriz.app.core.network.application.model.request.ApplicationRequest
import com.qriz.app.core.network.application.model.response.DdayResponse
import com.qriz.app.core.network.application.model.response.UserApplicationInfo
import com.qriz.core.data.application.application.ExamRepositoryImpl
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertTrue

class ExamRepositoryTest {
    private val mockApi = mockk<ApplicationApi>()
    private val repository = ExamRepositoryImpl(mockApi)

    // ========== getUserExams 테스트 ==========
    
    @Test
    fun `getUserExams_사용자정보존재_UserExam반환`() = runTest {
        // Given
        givenUserApplicationInfo()
        givenDdayInfo()

        // When
        val userExamFlow = repository.getUserExams()

        // Then
        userExamFlow.test {
            val result = awaitItem()
            assertTrue { result is ApiResult.Success }
            val data = (result as ApiResult.Success).data
            assertTrue { data != null }
            with(data!!) {
                examName shouldBe TEST_EXAM_NAME
                examDate shouldBe TEST_EXAM_DATE
                period shouldBe TEST_PERIOD
                dday shouldBe TEST_DDAY
                ddayType shouldBe DdayType.BEFORE
            }
        }
    }

    @Test
    fun `getUserExams_사용자정보없음_null반환`() = runTest {
        // Given
        coEvery { mockApi.getUserApplicationInfo() } returns ApiResult.Failure(
            code = -1,
            message = "유저 시험정보 없음"
        )

        // When
        val userExamFlow = repository.getUserExams()

        // Then
        userExamFlow.test {
            awaitItem() shouldBe ApiResult.Success(null)
        }
    }

    // ========== getExamSchedules 테스트 ==========
    
    @Test
    fun `getExamSchedules_시험일정존재_Schedule목록반환`() = runTest {
        // Given
        val examSchedules = listOf(
            ExamSchedule(
                applicationId = 1,
                examName = "SQLD",
                examDate = "3월 8일(토)",
                period = "02.03(월) 10:00 ~ 02.07(금) 18:00",
                userApplyId = null,
                releaseDate = "4월 4일",
            ),
            ExamSchedule(
                applicationId = 2,
                examName = "정보처리기사",
                examDate = "5월 31일(토)",
                period = "04.28(월) 10:00 ~ 05.02(금) 18:00",
                userApplyId = 123,
                releaseDate = "5월 5일",
            )
        )
        
        coEvery { mockApi.applications() } returns ApiResult.Success(
            ExamListResponse(
                registeredApplicationId = 1,
                registeredUserApplyId = 123,
                applications = examSchedules
            )
        )

        // When
        val result = repository.getExamSchedules()

        // Then
        coVerify { mockApi.applications() }
        assertTrue { result is ApiResult.Success }
        val schedules = (result as ApiResult.Success).data
        schedules.size shouldBe 2
        schedules shouldBe listOf(
            Schedule(
                applicationId = 1,
                userApplyId = null,
                examName = "SQLD",
                period = "02.03(월) 10:00 ~ 02.07(금) 18:00",
                examDate = "3월 8일(토)",
                releaseDate = "4월 4일",
                periodStart = LocalDateTime.of(
                    2025,
                    2,
                    3,
                    10,
                    0
                ),
                periodEnd = LocalDateTime.of(
                    2025,
                    2,
                    7,
                    18,
                    0
                )
            ),
            Schedule(
                applicationId = 2,
                userApplyId = 123,
                examName = "정보처리기사",
                period = "04.28(월) 10:00 ~ 05.02(금) 18:00",
                examDate = "5월 31일(토)",
                releaseDate = "5월 5일",
                periodStart = LocalDateTime.of(
                    2025,
                    4,
                    28,
                    10,
                    0
                ),
                periodEnd = LocalDateTime.of(
                    2025,
                    5,
                    2,
                    18,
                    0
                )
            )
        )
    }

    @Test
    fun `getExamSchedules_시험일정없음_빈목록반환`() = runTest {
        // Given
        coEvery { mockApi.applications() } returns ApiResult.Success(
            ExamListResponse(
                registeredApplicationId = null,
                registeredUserApplyId = null,
                applications = emptyList()
            )
        )
        givenUserApplicationInfo()
        givenDdayInfo()

        // When
        val result = repository.getExamSchedules()

        // Then
        assertTrue { result is ApiResult.Success }
        val schedules = (result as ApiResult.Success).data
        schedules.size shouldBe 0
    }

    // ========== applyExam 테스트 ==========
    
    @Test
    fun `applyExam_시험신청성공_Unit반환`() = runTest {
        // Given
        val examId = 123L
        coEvery { mockApi.apply(ApplicationRequest(applyId = examId)) } returns ApiResult.Success(
            UserApplicationInfo(
                examName = TEST_EXAM_NAME,
                period = TEST_PERIOD,
                examDate = TEST_EXAM_DATE,
                releaseDate = null
            )
        )
        givenUserApplicationInfo()
        givenDdayInfo()

        // When
        val result = repository.applyExam(examId)

        // Then
        coVerify { mockApi.apply(ApplicationRequest(applyId = examId)) }
        result shouldBe ApiResult.Success(Unit)
    }

    // ========== editExam 테스트 ==========
    
    @Test
    fun `editExam_시험변경성공_Unit반환`() = runTest {
        // Given
        val uaid = 456L
        val examId = 123L
        coEvery { mockApi.modify(uaid, ApplicationModifyRequest(newApplyId = examId)) } returns ApiResult.Success(
            UserApplicationInfo(
                examName = TEST_EXAM_NAME,
                period = TEST_PERIOD,
                examDate = TEST_EXAM_DATE,
                releaseDate = null
            )
        )
        givenUserApplicationInfo()
        givenDdayInfo()

        // When
        val result = repository.editExam(uaid, examId)

        // Then
        coVerify { mockApi.modify(uaid, ApplicationModifyRequest(newApplyId = examId)) }
        result shouldBe ApiResult.Success(Unit)
    }

    // ========== Helper Methods ==========
    
    private fun givenUserApplicationInfo() {
        coEvery { mockApi.getUserApplicationInfo() } returns ApiResult.Success(
            createUserApplicationInfo()
        )
    }

    private fun givenDdayInfo(dday: Int = TEST_DDAY, status: String = "BEFORE") {
        coEvery { mockApi.getDday() } returns ApiResult.Success(
            DdayResponse(
                dday = dday,
                status = status
            )
        )
    }

    private fun createUserApplicationInfo(
        examName: String = TEST_EXAM_NAME,
        examDate: String = TEST_EXAM_DATE,
        period: String = TEST_PERIOD,
        releaseDate: String? = null
    ) = UserApplicationInfo(
        examName = examName,
        examDate = examDate,
        period = period,
        releaseDate = releaseDate
    )

    companion object {
        private const val TEST_EXAM_NAME = "테스트 시험"
        private const val TEST_EXAM_DATE = "2023-10-10"
        private const val TEST_PERIOD = "05-10 ~ 05-15"
        private const val TEST_DDAY = 10
    }
}

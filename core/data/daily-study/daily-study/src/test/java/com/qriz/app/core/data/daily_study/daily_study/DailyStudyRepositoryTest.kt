package com.qriz.app.core.data.daily_study.daily_study

import app.cash.turbine.test
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlanDetail
import com.qriz.app.core.data.daily_study.daily_study_api.model.ImportanceLevel
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.model.SimplePlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.requireValue
import com.qriz.app.core.network.daily_study.DailyStudyApi
import com.qriz.app.core.network.daily_study.model.response.DailyStudyDetailResponse
import com.qriz.app.core.network.daily_study.model.response.DailyStudyPlanResponse
import com.qriz.app.core.network.daily_study.model.response.DailyStudyStatusResponse
import com.qriz.app.core.network.daily_study.model.response.DailyTestOptionResponse
import com.qriz.app.core.network.daily_study.model.response.DailyTestQuestionResponse
import com.qriz.app.core.network.daily_study.model.response.PlannedSkillResponse
import com.qriz.app.core.network.daily_study.model.response.SimplePlannedSkillResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponseContainer
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate
import com.qriz.app.core.data.test.test_api.model.Test as TestModel

class DailyStudyRepositoryTest {
    val mockApi = mockk<DailyStudyApi>()

    val repository = DailyStudyRepositoryImpl(mockApi)

    @Test
    fun `오늘의 공부 플랜을 불러와 flow형태로 반환한다`() = runTest {
        //given
        coEvery { mockApi.getDailyStudyPlan() } returns ApiResult.Success(
            listOf(
                DailyStudyPlanResponse(
                    id = 0,
                    planDate = "2025-07-01",
                    completionDate = null,
                    dayNumber = "Day1",
                    completed = false,
                    reviewDay = false,
                    comprehensiveReviewDay = false,
                    plannedSkills = listOf(
                        PlannedSkillResponse(
                            id = 1,
                            type = "SQL기본",
                            keyConcept = "WEHRE절",
                            description = "",
                        )
                    )
                )
            )
        )

        val expected = listOf(
            DailyStudyPlan(
                id = 0,
                planDate = LocalDate.parse("2025-07-01"),
                completionDate = null,
                completed = false,
                reviewDay = false,
                comprehensiveReviewDay = false,
                plannedSkills = listOf(
                    PlannedSkill(
                        id = 1,
                        type = "SQL기본",
                        keyConcept = "WEHRE절",
                        description = "",
                    )
                )
            )
        )

        //when
        val flow = repository.getDailyStudyPlanFlow()

        //then
        flow.test {
            awaitItem().requireValue shouldBe expected
            awaitComplete()
        }
    }

    @Test
    fun `주간 추천 개념을 불러와 flow형태로 반환한다`() = runTest {
        //given
        coEvery { mockApi.getWeeklyRecommendation() } returns ApiResult.Success(
            WeeklyRecommendationResponseContainer(
                recommendations = listOf(
                    WeeklyRecommendationResponse(
                        skillId = 1,
                        keyConcepts = "WEHRE절",
                        description = "",
                        importanceLevel = "상",
                        frequency = 1,
                        incorrectRate = 0.1,
                    )
                )
            )
        )
        val expected = listOf(
            WeeklyRecommendation(
                skillId = 1,
                keyConcepts = "WEHRE절",
                description = "",
                importanceLevel = ImportanceLevel.HIGH,
                frequency = 1,
                incorrectRate = 0.1,
            )
        )

        //when
        val flow = repository.getWeeklyRecommendation()

        //then
        flow.test {
            awaitItem().requireValue shouldBe expected
            awaitComplete()
        }
    }

    @Test
    fun `플랜을 초기화 할 수 있다`() = runTest {
        //given
        coEvery { mockApi.resetDailyStudyPlan() } returns ApiResult.Success(Unit)

        //when
        val result = repository.resetDailyStudyPlan()

        //then
        coVerify { mockApi.resetDailyStudyPlan() }
        result shouldBe ApiResult.Success(Unit)
    }

    @Test
    fun `일일 공부 플랜 상세 정보를 성공적으로 가져온다`() = runTest {
        //given
        val day = 1
        val mockResponse = DailyStudyDetailResponse(
            dayNumber = "Day1",
            skills = listOf(
                SimplePlannedSkillResponse(
                    id = 1,
                    keyConcept = "SQL 기본",
                    description = "SELECT문과 WHERE절 사용법"
                ),
                SimplePlannedSkillResponse(
                    id = 2,
                    keyConcept = "JOIN",
                    description = "INNER JOIN과 LEFT JOIN 차이점"
                )
            ),
            status = DailyStudyStatusResponse(
                attemptCount = 2,
                passed = true,
                retestEligible = false,
                totalScore = 85.5,
                available = true
            )
        )
        
        coEvery { mockApi.getDailyStudyDetail("1") } returns ApiResult.Success(mockResponse)

        val expected = DailyStudyPlanDetail(
            dayNumber = "Day1",
            skills = listOf(
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
            ),
            attemptCount = 2,
            passed = true,
            retestEligible = false,
            totalScore = 85.5,
            available = true
        )

        //when
        val result = repository.getDailyStudyPlanDetail(day)

        //then
        coVerify { mockApi.getDailyStudyDetail("1") }
        result shouldBe ApiResult.Success(expected)
    }

    @Test
    fun `getDailyStudy - 문제를 불러와서 Test로 변환해서 반환한다`() = runTest {
        //given
        val dayNumber = 1
        val mockResponse = listOf(
            DailyTestQuestionResponse(
                questionId = 1L,
                skillId = 1,
                category = 1,
                question = "SQL에서 WHERE절의 역할은 무엇인가요?",
                description = "WHERE절의 기본 개념을 묻는 문제입니다.",
                options = listOf(
                    DailyTestOptionResponse(id = 1L, content = "데이터를 조회한다"),
                    DailyTestOptionResponse(id = 2L, content = "데이터를 필터링한다"),
                    DailyTestOptionResponse(id = 3L, content = "데이터를 정렬한다")
                ),
                timeLimit = 60,
                difficulty = 1
            ),
            DailyTestQuestionResponse(
                questionId = 2L,
                skillId = 2,
                category = 1,
                question = "JOIN의 종류에는 어떤 것이 있나요?",
                description = "JOIN의 기본 개념을 묻는 문제입니다.",
                options = listOf(
                    DailyTestOptionResponse(id = 4L, content = "INNER JOIN"),
                    DailyTestOptionResponse(id = 5L, content = "LEFT JOIN"),
                    DailyTestOptionResponse(id = 6L, content = "모든 답이 맞다")
                ),
                timeLimit = 90,
                difficulty = 2
            )
        )
        
        coEvery { mockApi.getDailyStudy("1") } returns ApiResult.Success(mockResponse)

        val expectedTest = TestModel(
            questions = listOf(
                Question(
                    id = 1L,
                    question = "SQL에서 WHERE절의 역할은 무엇인가요?",
                    options = listOf(
                        Option(id = 1L, content = "데이터를 조회한다"),
                        Option(id = 2L, content = "데이터를 필터링한다"),
                        Option(id = 3L, content = "데이터를 정렬한다")
                    ),
                    timeLimit = 60,
                    description = "WHERE절의 기본 개념을 묻는 문제입니다.",
                    skillId = 1,
                    category = 1,
                    difficulty = 1
                ),
                Question(
                    id = 2L,
                    question = "JOIN의 종류에는 어떤 것이 있나요?",
                    options = listOf(
                        Option(id = 4L, content = "INNER JOIN"),
                        Option(id = 5L, content = "LEFT JOIN"),
                        Option(id = 6L, content = "모든 답이 맞다")
                    ),
                    timeLimit = 90,
                    description = "JOIN의 기본 개념을 묻는 문제입니다.",
                    skillId = 2,
                    category = 1,
                    difficulty = 2
                )
            ),
            totalTimeLimit = 150
        )

        //when
        val result = repository.getDailyStudy(dayNumber)

        //then
        coVerify { mockApi.getDailyStudy("1") }
        result shouldBe ApiResult.Success(expectedTest)
    }

    @Test
    fun `submitTest - 풀이를 제출할 수 있다`() = runTest {
        //given
        val day = 1
        val activities = listOf(
            Triple(1L, Option(id = 1L, content = "데이터를 조회한다"), 30),
            Triple(2L, Option(id = 4L, content = "INNER JOIN"), 45)
        )
        
        coEvery { mockApi.submitDailyTest(any(), any()) } returns ApiResult.Success(Unit)

        //when
        val result = repository.submitTest(day, activities)

        //then
        coVerify { 
            mockApi.submitDailyTest(
                dayNumber = 1,
                request = any()
            )
        }
        result shouldBe ApiResult.Success(Unit)
    }
}

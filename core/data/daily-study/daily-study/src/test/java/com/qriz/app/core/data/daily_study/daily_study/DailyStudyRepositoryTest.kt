package com.qriz.app.core.data.daily_study.daily_study

import app.cash.turbine.test
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.ImportanceLevel
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.requireValue
import com.qriz.app.core.network.daily_study.DailyStudyApi
import com.qriz.app.core.network.daily_study.model.response.DailyStudyPlanResponse
import com.qriz.app.core.network.daily_study.model.response.PlannedSkillResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponseContainer
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate

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
}

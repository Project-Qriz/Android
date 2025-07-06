package com.qriz.app.core.data.daily_study.daily_study

import com.qriz.app.core.data.daily_study.daily_study.mapper.toDailyStudyDetail
import com.qriz.app.core.data.daily_study.daily_study.mapper.toDailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study.mapper.toTest
import com.qriz.app.core.data.daily_study.daily_study.mapper.toWeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlanDetail
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.data.test.test_api.model.TestCategory
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.map
import com.qriz.app.core.network.daily_study.DailyStudyApi
import com.qriz.app.core.network.daily_study.model.request.DailyTestSubmitActivity
import com.qriz.app.core.network.daily_study.model.request.DailyTestSubmitQuestion
import com.qriz.app.core.network.daily_study.model.request.DailyTestSubmitRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DailyStudyRepositoryImpl @Inject constructor(
    private val dailyStudyApi: DailyStudyApi
): DailyStudyRepository {
    override fun getDailyStudyPlanFlow(): Flow<ApiResult<List<DailyStudyPlan>>> = flow {
        emit(dailyStudyApi.getDailyStudyPlan().map { it.toDailyStudyPlan() })
    }

    override fun getWeeklyRecommendation(): Flow<ApiResult<List<WeeklyRecommendation>>> = flow {
        emit(dailyStudyApi.getWeeklyRecommendation().map { it.toWeeklyRecommendation() })
    }

    override suspend fun resetDailyStudyPlan(): ApiResult<Unit> {
        return dailyStudyApi.resetDailyStudyPlan()
    }

    override suspend fun getDailyStudyPlanDetail(day: Int): ApiResult<DailyStudyPlanDetail> {
        return dailyStudyApi
            .getDailyStudyDetail(day.toString())
            .map { it.toDailyStudyDetail() }
    }

    override suspend fun getDailyStudy(dayNumber: Int): ApiResult<Test> {
        return dailyStudyApi.getDailyStudy(dayNumber.toString()).map { it.toTest() }
    }

    override suspend fun submitTest(day: Int, activities: List<Triple<Long, Option, Int>>): ApiResult<Unit> {
        val request = DailyTestSubmitRequest(
            activities = activities.mapIndexed { index, activity ->
                DailyTestSubmitActivity(
                    question = DailyTestSubmitQuestion(
                        questionId = activity.first,
                        category = TestCategory.DAILY_STUDY.id,
                    ),
                    optionId = activity.second.id,
                    questionNum = index + 1,
                    timeSpent = activity.third
                )
            }
        )

        return dailyStudyApi.submitDailyTest(
            dayNumber = day,
            request = request
        )
    }
}

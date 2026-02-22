package com.qriz.app.core.data.daily_study.daily_study_api.repository

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlanDetail
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyTestResult
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyReviewResult
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface DailyStudyRepository {
    val planUpdateFlow: SharedFlow<Unit>

    fun getDailyStudyPlanFlow(): Flow<ApiResult<List<DailyStudyPlan>>>

    fun getWeeklyRecommendation(): Flow<ApiResult<List<WeeklyRecommendation>>>

    suspend fun resetDailyStudyPlan(): ApiResult<Unit>

    suspend fun getDailyStudyPlanDetail(day: Int): ApiResult<DailyStudyPlanDetail>

    suspend fun getDailyStudy(dayNumber: Int): ApiResult<Test>

    suspend fun submitTest(day: Int, activities: List<Triple<Long, Option, Int>>): ApiResult<Unit>

    suspend fun getDailyTestResult(day: Int): ApiResult<DailyTestResult>

    suspend fun getWeeklyReviewResult(day: Int): ApiResult<WeeklyReviewResult>
}

package com.qriz.app.core.data.daily_study.daily_study_api.repository

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface DailyStudyRepository {
    fun getDailyStudyPlanFlow(): Flow<ApiResult<List<DailyStudyPlan>>>

    fun getWeeklyRecommendation(): Flow<ApiResult<List<WeeklyRecommendation>>>

    suspend fun resetDailyStudyPlan(): ApiResult<Unit>
}

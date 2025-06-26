package com.qriz.app.core.data.daily_study.daily_study

import com.qriz.app.core.data.daily_study.daily_study.mapper.toDailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study.mapper.toWeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.map
import com.qriz.app.core.network.daily_study.DailyStudyApi
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
}

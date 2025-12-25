package com.qriz.app.core.domain.usecase_api.daily_study

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface GetDailyStudyPlanUseCase {
    operator fun invoke(): Flow<ApiResult<List<DailyStudyPlan>>>
}

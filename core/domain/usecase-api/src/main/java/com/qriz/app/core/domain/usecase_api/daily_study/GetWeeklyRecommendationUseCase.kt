package com.qriz.app.core.domain.usecase_api.daily_study

import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface GetWeeklyRecommendationUseCase {
    operator fun invoke(): Flow<ApiResult<List<WeeklyRecommendation>>>
}
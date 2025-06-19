package com.qriz.app.core.network.daily_study

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.daily_study.model.response.DailyStudyPlanResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponseContainer
import retrofit2.http.GET

interface DailyStudyApi {
    @GET("/api/v1/daily/plan")
    suspend fun getDailyStudyPlan(): ApiResult<List<DailyStudyPlanResponse>>

    @GET("/api/v1/recommend/weekly")
    suspend fun getWeeklyRecommendation(): ApiResult<WeeklyRecommendationResponseContainer>
}

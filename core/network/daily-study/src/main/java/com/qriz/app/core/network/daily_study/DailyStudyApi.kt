package com.qriz.app.core.network.daily_study

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.daily_study.model.request.DailyTestSubmitRequest
import com.qriz.app.core.network.daily_study.model.response.DailyStudyDetailResponse
import com.qriz.app.core.network.daily_study.model.response.DailyStudyPlanResponse
import com.qriz.app.core.network.daily_study.model.response.WeeklyRecommendationResponseContainer
import com.qriz.app.core.network.daily_study.model.response.DailyTestQuestionResponse
import com.qriz.app.core.network.daily_study.model.response.DailyTestResultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DailyStudyApi {
    @GET("/api/v1/daily/plan")
    suspend fun getDailyStudyPlan(): ApiResult<List<DailyStudyPlanResponse>>

    @GET("/api/v1/recommend/weekly")
    suspend fun getWeeklyRecommendation(): ApiResult<WeeklyRecommendationResponseContainer>

    @POST("/api/v1/daily/regenerate")
    suspend fun resetDailyStudyPlan(): ApiResult<Unit>

    @GET("/api/v1/daily/detail-status/{dayNumber}")
    suspend fun getDailyStudyDetail(@Path("dayNumber") dayNumber: String): ApiResult<DailyStudyDetailResponse>

    @GET("/api/v1/daily/get/{dayNumber}")
    suspend fun getDailyStudy(@Path("dayNumber") dayNumber: String): ApiResult<List<DailyTestQuestionResponse>>

    @POST("/api/v1/daily/submit/{dayNumber}")
    suspend fun submitDailyTest(
        @Path("dayNumber") dayNumber: Int,
        @Body request: DailyTestSubmitRequest,
    ): ApiResult<Unit>

    @GET("/api/v1/daily/subject-details/{dayNumber}")
    suspend fun getDailyStudyResult(@Path("dayNumber") dayNumber: Int): ApiResult<DailyTestResultResponse>
}

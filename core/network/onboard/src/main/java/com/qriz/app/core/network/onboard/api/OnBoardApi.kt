package com.qriz.app.core.network.onboard.api

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.onboard.model.request.SurveyRequest
import com.qriz.app.core.network.onboard.model.request.TestSubmitRequest
import com.qriz.app.core.network.onboard.model.response.AnalyzePreviewResponse
import com.qriz.app.core.network.onboard.model.response.PreviewTestListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OnBoardApi {
    @POST("/api/v1/survey")
    suspend fun submitSurvey(
        @Body request: SurveyRequest
    ): ApiResult<Unit>

    @GET("/api/v1/preview/get")
    suspend fun getPreviewTest(): ApiResult<PreviewTestListResponse>

    @POST("api/v1/preview/submit")
    suspend fun submitPreviewTest(@Body request: TestSubmitRequest): ApiResult<Unit>

    @GET("/api/v1/preview/analyze")
    suspend fun getPreviewTestResult(): ApiResult<AnalyzePreviewResponse>
}

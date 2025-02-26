package com.qriz.app.core.network.onboard.api

import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.onboard.model.request.SurveyRequest
import com.qriz.app.core.network.onboard.model.request.TestSubmitRequest
import com.qriz.app.core.network.onboard.model.response.AnalyzePreviewResponse
import com.qriz.app.core.network.onboard.model.response.PreviewTestListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OnBoardApi {
    @POST("/api/v1/survey")
    fun submitSurvey(
        @Body request: SurveyRequest
    ): NetworkResponse<Unit>

    @GET("/api/v1/preview/get")
    fun getPreviewTest(): NetworkResponse<PreviewTestListResponse>

    @GET("api/v1/preview/submit")
    fun submitPreviewTest(request: TestSubmitRequest): NetworkResponse<Unit>

    @GET("/api/v1/preview/analyze")
    fun getPreviewTestResult(): NetworkResponse<AnalyzePreviewResponse>
}

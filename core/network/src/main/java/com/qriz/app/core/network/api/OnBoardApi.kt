package com.qriz.app.core.network.api

import com.qriz.app.core.network.model.NetworkResponse
import com.qriz.app.core.network.model.request.user.SurveyRequest
import com.qriz.app.core.network.model.response.preview.PreviewTestListResponse
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
}

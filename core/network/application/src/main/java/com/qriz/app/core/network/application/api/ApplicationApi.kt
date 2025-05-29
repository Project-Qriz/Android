package com.qriz.app.core.network.application.api

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.application.model.ApplicationData
import com.qriz.app.core.network.application.model.request.ApplicationModifyRequest
import com.qriz.app.core.network.application.model.request.ApplicationRequest
import com.qriz.app.core.network.application.model.response.DdayResponse
import com.qriz.app.core.network.application.model.response.UserApplicationInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApplicationApi {
    @GET("/api/v1/applications")
    suspend fun applications(): ApiResult<ApplicationData>

    @POST("/api/v1/applications")
    suspend fun apply(
        @Body request: ApplicationRequest
    ): ApiResult<UserApplicationInfo>

    @PATCH("/api/v1/applications/{uaid}")
    suspend fun modify(
        @Path("uaid") id: Long,
        @Body request: ApplicationModifyRequest
    ): ApiResult<UserApplicationInfo>

    @GET("/api/v1/applications/applied")
    suspend fun getUserApplicationInfo(): ApiResult<UserApplicationInfo>

    @GET("/api/v1/applications/d-day")
    suspend fun getDday(): ApiResult<DdayResponse>
}

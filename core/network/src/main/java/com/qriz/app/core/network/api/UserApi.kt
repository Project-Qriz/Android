package com.qriz.app.core.network.api

import com.qriz.app.core.network.model.NetworkResponse
import com.qriz.app.core.network.model.request.user.JoinRequest
import com.qriz.app.core.network.model.request.user.LoginRequest
import com.qriz.app.core.network.model.response.user.JoinResponse
import com.qriz.app.core.network.model.response.user.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("/api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): NetworkResponse<LoginResponse>

    @POST("/api/join")
    suspend fun signUp(
        @Body request: JoinRequest
    ): NetworkResponse<JoinResponse>
}

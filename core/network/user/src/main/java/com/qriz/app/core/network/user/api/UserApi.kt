package com.qriz.app.core.network.user.api

import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.network.user.model.response.JoinResponse
import com.qriz.app.core.network.user.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): NetworkResponse<LoginResponse>

    @POST("/api/join")
    suspend fun signUp(
        @Body request: JoinRequest
    ): NetworkResponse<JoinResponse>
}
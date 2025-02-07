package com.qriz.app.core.network.user.api

import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.user.model.request.ResetPwdRequest
import com.qriz.app.core.network.user.model.request.FindIdRequest
import com.qriz.app.core.network.user.model.request.FindPwdRequest
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.network.user.model.request.VerifyPwdResetRequest
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

    @POST("/api/find-username")
    suspend fun sendEmailToFindId(
        @Body request: FindIdRequest
    ): NetworkResponse<Unit>

    @POST("/api/find-pwd")
    suspend fun sendEmailToPwd(
        @Body request: FindPwdRequest
    ): NetworkResponse<Unit>

    @POST("/api/verify-pwd-reset")
    suspend fun verifyPwdReset(
        @Body request: VerifyPwdResetRequest
    ): NetworkResponse<Unit>

    @POST("/api/pwd-reset")
    suspend fun resetPwd(
        @Body request: ResetPwdRequest
    ): NetworkResponse<Unit>
}

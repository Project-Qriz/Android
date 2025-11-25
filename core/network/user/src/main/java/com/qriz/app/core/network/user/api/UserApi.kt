package com.qriz.app.core.network.user.api

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.user.model.request.EmailAuthenticationRequest
import com.qriz.app.core.network.user.model.request.FindIdRequest
import com.qriz.app.core.network.user.model.request.FindPwdRequest
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.network.user.model.request.ResetPwdRequest
import com.qriz.app.core.network.user.model.request.SingleEmailRequest
import com.qriz.app.core.network.user.model.request.VerifyPwdResetRequest
import com.qriz.app.core.network.user.model.response.DuplicateResponse
import com.qriz.app.core.network.user.model.response.UserProfileResponse
import com.qriz.app.core.network.user.model.response.JoinResponse
import com.qriz.app.core.network.user.model.response.LoginResponse
import com.qriz.app.core.network.user.model.response.VerifyPwdResetRespoonse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("/api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResult<LoginResponse>

    @GET("/api/v1/user/info")
    suspend fun getUserProfile(
    ): ApiResult<UserProfileResponse>

    @POST("/api/email-send")
    suspend fun sendAuthEmail(
        @Body request: SingleEmailRequest
    ): ApiResult<Unit>

    @POST("/api/email-authentication")
    suspend fun verifyEmailAuthenticationNumber(
        @Body request: EmailAuthenticationRequest
    ): ApiResult<Unit>

    @GET("/api/username-duplicate")
    suspend fun checkDuplicateId(
        @Query("username") userId: String
    ): ApiResult<DuplicateResponse>

    @POST("/api/join")
    suspend fun signUp(
        @Body request: JoinRequest
    ): ApiResult<JoinResponse>

    @POST("/api/find-username")
    suspend fun sendEmailToFindId(
        @Body request: FindIdRequest
    ): ApiResult<Unit>

    @POST("/api/find-pwd")
    suspend fun sendEmailToPwd(
        @Body request: FindPwdRequest
    ): ApiResult<Unit>

    @POST("/api/verify-pwd-reset")
    suspend fun verifyPwdReset(
        @Body request: VerifyPwdResetRequest
    ): ApiResult<VerifyPwdResetRespoonse>

    @POST("/api/pwd-reset")
    suspend fun resetPwd(
        @Body request: ResetPwdRequest
    ): ApiResult<Unit>

    @DELETE("/api/v1/withdraw")
    suspend fun withdraw(): ApiResult<Unit>
}

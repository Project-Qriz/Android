package com.quiz.app.core.data.user.user_api.repository

import com.qriz.app.core.model.ApiResult
import com.quiz.app.core.data.user.user_api.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(
        id: String,
        password: String,
    ): ApiResult<User>

    fun getUserFlow(): Flow<User>

    suspend fun getUser(): ApiResult<User>

    suspend fun requestEmailAuthNumber(email: String): ApiResult<Unit>

    suspend fun verifyEmailAuthNumber(email: String, authenticationNumber: String): ApiResult<Unit>

    suspend fun isNotDuplicateId(id: String): ApiResult<Boolean>

    suspend fun signUp(
        loginId: String,
        password: String,
        email: String,
        nickname: String,
    ): ApiResult<User>

    suspend fun sendEmailToFindId(email: String): ApiResult<Unit>

    suspend fun sendEmailToFindPassword(email: String): ApiResult<Unit>

    suspend fun verifyPasswordAuthNumber(email: String, authNumber: String): ApiResult<String>

    suspend fun resetPassword(password: String, resetToken: String): ApiResult<Unit>

    suspend fun logout()

    suspend fun withdraw(): ApiResult<Unit>
}

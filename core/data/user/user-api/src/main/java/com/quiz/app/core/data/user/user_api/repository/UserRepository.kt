package com.quiz.app.core.data.user.user_api.repository

import com.quiz.app.core.data.user.user_api.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val flowLogin: Flow<Boolean>

    suspend fun login(
        id: String,
        password: String,
    ): User

    suspend fun sendAuthenticationNumber(email: String): Boolean

    suspend fun verifyAuthenticationNumber(authenticationNumber: String): Boolean

    suspend fun checkDuplicateId(id: String): Boolean

    suspend fun signUp(
        loginId: String,
        password: String,
        email: String,
        nickname: String,
    ): User
}
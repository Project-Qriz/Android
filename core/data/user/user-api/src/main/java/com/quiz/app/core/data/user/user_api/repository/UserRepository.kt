package com.quiz.app.core.data.user.user_api.repository

import com.quiz.app.core.data.user.user_api.model.User

interface UserRepository {
    suspend fun login(
        id: String,
        password: String,
    ): User

    suspend fun sendAuthenticationNumber(email: String)

    suspend fun verifyAuthenticationNumber(authenticationNumber: String): Boolean

    suspend fun isNotDuplicateId(id: String): Boolean

    suspend fun signUp(
        loginId: String,
        password: String,
        email: String,
        nickname: String,
    ): User
}

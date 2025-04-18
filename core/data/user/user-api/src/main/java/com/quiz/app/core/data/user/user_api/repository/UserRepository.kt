package com.quiz.app.core.data.user.user_api.repository

import com.quiz.app.core.data.user.user_api.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(
        id: String,
        password: String,
    ): User

    fun getUserFlow(): Flow<User>

    suspend fun getUser(): User

    suspend fun requestEmailAuthNumber(email: String)

    suspend fun verifyEmailAuthNumber(email: String, authenticationNumber: String): Boolean

    suspend fun isNotDuplicateId(id: String): Boolean

    suspend fun signUp(
        loginId: String,
        password: String,
        email: String,
        nickname: String,
    ): User

    suspend fun sendEmailToFindId(email: String)

    suspend fun sendEmailToFindPassword(email: String)

    suspend fun verifyPasswordAuthNumber(authNumber: String)

    suspend fun resetPassword(password: String)
}

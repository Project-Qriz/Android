package com.qriz.app.core.data.repository

import com.qriz.app.core.data.model.user.User
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

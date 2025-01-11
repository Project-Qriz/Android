package com.qriz.core.data.token.token_api

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    val flowTokenExist: Flow<Boolean>

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun saveToken(accessToken: String, refreshToken: String)

    suspend fun clearToken()
}

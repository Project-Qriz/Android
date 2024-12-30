package com.qriz.core.data.token.token_api

interface TokenRepository {
    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun saveToken(accessToken: String, refreshToken: String)

    suspend fun clearToken()
}

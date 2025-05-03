package com.qriz.core.data.token.token_api

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    val flowTokenExist: Flow<Boolean>

    suspend fun isTokenExist(): Boolean

    suspend fun getAccessToken(): String?

    suspend fun saveToken(accessToken: String)

    suspend fun clearToken()
}

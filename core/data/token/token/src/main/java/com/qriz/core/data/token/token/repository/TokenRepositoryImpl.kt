package com.qriz.core.data.token.token.repository

import com.qriz.app.core.datastore.TokenDataStore
import com.qriz.core.data.token.token_api.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
): TokenRepository {
    override suspend fun getAccessToken(): String? = tokenDataStore.getAccessToken().ifEmpty { null }

    override suspend fun getRefreshToken(): String? = tokenDataStore.getRefreshToken().ifEmpty { null }

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        tokenDataStore.saveToken(accessToken = accessToken, refreshToken = refreshToken)
    }

    override suspend fun clearToken() {
        tokenDataStore.clearToken()
    }
}

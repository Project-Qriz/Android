package com.qriz.core.data.token.token.repository

import com.qriz.app.core.datastore.TokenDataStore
import com.qriz.core.data.token.token_api.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
): TokenRepository {
    override val flowTokenExist: Flow<Boolean> = tokenDataStore.flowAccessToken()
        .map { it.isNotEmpty() }

    override suspend fun getAccessToken(): String? = tokenDataStore.flowAccessToken()
        .map { token -> token.ifEmpty { null } }
        .first()

    override suspend fun getRefreshToken(): String? = tokenDataStore.flowRefreshToken()
        .map { token -> token.ifEmpty { null } }
        .first()

    override suspend fun saveToken(accessToken: String, refreshToken: String) {
        tokenDataStore.saveToken(accessToken = accessToken, refreshToken = refreshToken)
    }

    override suspend fun clearToken() {
        tokenDataStore.clearToken()
    }
}

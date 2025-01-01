package com.qriz.app.core.network.core.interceptor

import com.qriz.app.core.network.core.const.ACCESS_TOKEN_HEADER_KEY
import com.qriz.app.core.network.core.const.REFRESH_TOKEN_HEADER_KEY
import com.qriz.core.data.token.token_api.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { tokenRepository.getAccessToken() }

        val newRequest = chain.request().newBuilder().apply {
            if (accessToken != null) {
                header(ACCESS_TOKEN_HEADER_KEY, accessToken)
            }
        }.build()

        val response = chain.proceed(newRequest)

        val newAccessToken = response.header(ACCESS_TOKEN_HEADER_KEY)
        val newRefreshToken = response.header(REFRESH_TOKEN_HEADER_KEY)

        if (newAccessToken != null && newRefreshToken != null) {
            runBlocking {
                tokenRepository.saveToken(newAccessToken, newRefreshToken)
            }
        }

        return response
    }
}

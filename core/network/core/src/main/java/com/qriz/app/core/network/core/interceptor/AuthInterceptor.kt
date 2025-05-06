package com.qriz.app.core.network.core.interceptor

import com.qriz.app.core.network.core.const.ACCESS_TOKEN_HEADER_KEY
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
                header(ACCESS_TOKEN_HEADER_KEY, "$TOKEN_PREFIX$accessToken")
            }
        }.build()

        val response = chain.proceed(newRequest)
        val newAccessToken = response.header(ACCESS_TOKEN_HEADER_KEY)

        if (newAccessToken != null) {
            runBlocking {
                val value = newAccessToken.removePrefix(TOKEN_PREFIX)
                tokenRepository.saveToken(value)
            }
        }

        //AccessToken이 있지만 서버에서 갱신이 안되었을 경우 토큰 삭제
        if (accessToken != null && response.code == 401) {
            runBlocking { tokenRepository.clearToken() }
        }

        return response
    }

    companion object {
        const val TOKEN_PREFIX = "Bearer "
    }
}

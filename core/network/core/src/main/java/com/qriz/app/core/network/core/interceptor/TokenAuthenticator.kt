package com.qriz.app.core.network.core.interceptor

import com.qriz.app.core.network.core.const.REFRESH_TOKEN_HEADER_KEY
import com.qriz.core.data.token.token_api.TokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val isTokenExist = runBlocking { tokenRepository.flowTokenExist.first() }
        return if (isTokenExist) {
            val refreshToken = runBlocking { tokenRepository.getRefreshToken() }
            refreshToken?.let {
                response.request
                    .newBuilder()
                    .header(REFRESH_TOKEN_HEADER_KEY, refreshToken)
                    .build()
            }
            //TODO: refresh token null일 때 로그아웃 처리 등 필요
        } else {
            null
        }
    }
}

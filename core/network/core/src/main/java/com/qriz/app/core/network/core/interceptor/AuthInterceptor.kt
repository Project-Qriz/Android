package com.qriz.app.core.network.core.interceptor

import com.qriz.app.core.network.core.auth.AuthManager
import com.qriz.app.core.network.core.const.ACCESS_TOKEN_HEADER_KEY
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val json: Json,
    private val authManager: AuthManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = authManager.getAccessToken()

        val authedRequest = chain.request().newBuilder().apply {
            if (accessToken != null) {
                header(
                    ACCESS_TOKEN_HEADER_KEY,
                    "$TOKEN_PREFIX$accessToken"
                )
            }
        }.build()

        val response = chain.proceed(authedRequest)

        if (accessToken != null && response.code == 401) {
            synchronized(this) {
                val responseBody = response.body
                val bodyString = responseBody?.string() ?: ""
                val contentType = responseBody?.contentType()

                val newResponseBody = bodyString.toResponseBody(contentType)
                val newResponse = response.newBuilder()
                    .body(newResponseBody)
                    .build()

                val detailCode = getDetailCode(bodyString)
                if (detailCode != ACCESS_EXPIRED_CODE) {
                    //TODO: 로그아웃 로직
                    authManager.clearToken()
                    return newResponse
                }
                val stored = authManager.getAccessToken()
                if (accessToken == stored) {
                    try {
                        val refreshToken = authManager.getRefreshToken()
                            ?: run {
                                authManager.clearToken()
                                return newResponse
                            }

                        val result = authManager.renewToken(refreshToken)
                        val newRequest = authedRequest.newRequest(result)
                        return chain.proceed(newRequest)
                    } catch (e: Exception) {
                        authManager.clearToken()
                        return newResponse
                    }
                } else {
                    val newToken = authManager.getAccessToken()
                        ?: throw IllegalStateException("토큰이 갱신되지 않았습니다.")
                    val newRequest = authedRequest.newRequest(newToken)
                    return chain.proceed(newRequest)
                }
            }
        }

        return response
    }

    private fun Request.newRequest(accessToken: String): Request {
        return newBuilder().apply {
            header(ACCESS_TOKEN_HEADER_KEY, "$TOKEN_PREFIX$accessToken")
        }.build()
    }

    private fun getDetailCode(bodyString: String): Int {
        if (bodyString.isEmpty()) return 0
        return try {
            val response = json.parseToJsonElement(bodyString).jsonObject
            response["detailCode"]?.toString()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    companion object {
        const val TOKEN_PREFIX = "Bearer "
        const val ACCESS_EXPIRED_CODE = 3
    }
}

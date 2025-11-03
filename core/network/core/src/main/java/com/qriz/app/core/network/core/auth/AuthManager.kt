package com.qriz.app.core.network.core.auth

import android.util.Log
import com.qriz.app.core.network.core.BuildConfig
import com.qriz.core.data.token.token_api.TokenRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named

class AuthManager @Inject constructor(
    @Named("AuthOkHttpClient") private val okHttpClient: OkHttpClient,
    private val tokenRepository: TokenRepository,
    private val json: Json
) {
    fun getAccessToken(): String? = runBlocking { tokenRepository.getAccessToken() }

    fun getRefreshToken(): String? = runBlocking { tokenRepository.getRefreshToken() }

    fun clearToken() = runBlocking { tokenRepository.clearToken() }

    fun renewToken(refreshToken: String): String {
        var lastException: Exception? = null

        repeat(MAX_RETRY_COUNT) { attemptCount ->
            try {
                val requestBody = json.encodeToString(
                    mapOf("refreshToken" to refreshToken)
                ).toRequestBody("application/json".toMediaType())

                val oldAccessToken = getAccessToken() ?: throw IllegalStateException("AccessToken is null")

                val request = Request.Builder()
                    .header("Authorization", "Bearer $oldAccessToken")
                    .url(RENEW_TOKEN_URL).post(requestBody).build()

                val response = okHttpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody =
                        response.body?.string() ?: throw IllegalStateException("응답 body가 null입니다.")

                    val apiResponse = json.decodeFromString<ApiResponse>(responseBody)

                    if (apiResponse.code == 1) {
                        val newAccessToken = response.header("Authorization")?.removePrefix("Bearer ")
                            ?: throw IllegalStateException("Authorization 헤더가 없습니다.")
                        runBlocking {
                            tokenRepository.saveToken(
                                accessToken = newAccessToken,
                                refreshToken = apiResponse.data.refreshToken ?: refreshToken
                            )
                        }
                        return newAccessToken
                    } else {
                        throw IllegalStateException("토큰 갱신 실패: ${apiResponse.msg}")
                    }
                } else {
                    val errorBody = response.body?.string() ?: "Unknown error"
                    throw IllegalStateException("토큰 갱신 실패 (HTTP ${response.code}): $errorBody")
                }
            } catch (e: Exception) {
                lastException = e

                if (attemptCount < MAX_RETRY_COUNT - 1) {
                    Thread.sleep(RETRY_DELAY_MS)
                }
            }
        }

        throw lastException ?: IllegalStateException("토큰 갱신에 실패했습니다.")
    }

    @Serializable
    private data class ApiResponse(
        @SerialName("code") val code: Int,
        @SerialName("msg") val msg: String,
        @SerialName("data") val data: DataResponse
    )

    @Serializable
    private data class DataResponse(
        @SerialName("rotated") val rotated: Boolean,
        @SerialName("refreshToken") val refreshToken: String? = null,
        @SerialName("refreshExpiry") val refreshExpiry: String
    )

    companion object {
        private const val RENEW_TOKEN_URL = BuildConfig.BASE_URL + "/api/v1/auth/token/refresh"
        private const val MAX_RETRY_COUNT = 3
        private const val RETRY_DELAY_MS = 300L
    }
}

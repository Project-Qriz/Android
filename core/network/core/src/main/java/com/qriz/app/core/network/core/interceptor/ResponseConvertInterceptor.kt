package com.qriz.app.core.network.core.interceptor

import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class ResponseConvertInterceptor @Inject constructor(
    private val json: Json
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseContentType = response.body?.contentType()

        val newBody = if (response.isSuccessful) {
            val bodyString = response.body?.string() ?: ""
            val path = request.url.pathSegments.joinToString("/")
            val prunedBody = pruneField(bodyString).toResponseBody(responseContentType)

            if (path == LOGIN_API_PATH || path == SOCIAL_LOGIN_API_PATH) {
                val accessToken = response.headers["Authorization"]?.removePrefix("Bearer ") ?: ""
                convertAuthResponse(
                    accessToken = accessToken,
                    body = prunedBody,
                    contentType = responseContentType
                )
            } else {
                prunedBody
            }
        } else {
            val bodyString = response.body?.string() ?: ""
            bodyString.toResponseBody(responseContentType)
        }

        return response.newResponse(newBody)
    }

    private fun Response.newResponse(newBody: ResponseBody?): Response = newBuilder()
        .body(newBody)
        .build()

    /**
     * Token Refresh or Login의 경우 Header에 AccessToken이 달려오기 때문에 응답값에 넣어주기 위해 따로 변형한다.
     * @param path pathSegment를 이어붙혀 엔드포인드를 확인하는 용도다.
     * @param body 이미 ApiResult를 위해 변형된 값이다.
     * @param contentType Response의 contentType
     */
    private fun convertAuthResponse(
        accessToken: String,
        body: ResponseBody?,
        contentType: MediaType?
    ): ResponseBody? {
        if (body == null) return null

        val bodyString = body.string()
        val jsonObject = json.parseToJsonElement(bodyString).jsonObject
        val newJsonObject = JsonObject(jsonObject.toMutableMap().apply {
            put("accessToken", JsonPrimitive(accessToken))
        })
        return newJsonObject.toString().toResponseBody(contentType)
    }

    private fun pruneField(rawBody: String): String {
        val jsonObject = json.parseToJsonElement(rawBody).jsonObject
        val data = jsonObject["data"]

        return data?.toString() ?: "{}"
    }

    companion object {
        private const val LOGIN_API_PATH = "api/login"
        private const val SOCIAL_LOGIN_API_PATH = "api/auth/social/login"
    }
}

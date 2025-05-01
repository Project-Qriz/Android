package com.qriz.app.core.network.core.interceptor

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class ResponseConvertInterceptor @Inject constructor(
    private val json: Json
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val contentType = request.body?.contentType()

        val newBody = if (response.isSuccessful) {
            val responseBody = response.body

            responseBody?.let { pruneField(it.string()) }?.toResponseBody(contentType)
        } else {
            response.body
        }

        return response.newBuilder().body(newBody).build()
    }

    private fun pruneField(rawBody: String): String {
        val jsonObject = json.parseToJsonElement(rawBody).jsonObject
        val data = jsonObject["data"]

        return data?.toString() ?: "{}"
    }
}

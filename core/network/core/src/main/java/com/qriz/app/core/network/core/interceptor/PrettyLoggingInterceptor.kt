package com.qriz.app.core.network.core.interceptor

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.Companion.INFO
import okhttp3.internal.platform.Platform.Companion.WARN
import okhttp3.logging.HttpLoggingInterceptor

class PrettyLoggingInterceptor : HttpLoggingInterceptor.Logger {
    private val json = Json { prettyPrint = true }

    override fun log(message: String) {
        val trimMessage = message.trim()

        if ((trimMessage.startsWith("{") && trimMessage.endsWith("}"))
            || (trimMessage.startsWith("[") && trimMessage.endsWith("]"))) {
            try {
                val prettyJson = json.encodeToString(json.parseToJsonElement(message))
                Platform.get().log(prettyJson, INFO, null)
            } catch (e: Exception) {
                Platform.get().log(message, WARN, e)
            }
        } else {
            Platform.get().log(message, INFO, null)
        }
    }
}

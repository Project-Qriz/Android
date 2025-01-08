package com.qriz.app.core.network.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    @SerialName("code") val code: Int,
    @SerialName("msg") val message: String,
    @SerialName("data") val data: T,
)

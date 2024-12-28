package com.qriz.app.core.network.common

data class NetworkResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)
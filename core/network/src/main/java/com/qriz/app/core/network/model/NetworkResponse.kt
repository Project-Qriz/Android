package com.qriz.app.core.network.model

data class NetworkResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)

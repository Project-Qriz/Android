package com.qriz.app.core.network.model.response.user

data class ApplyListResponse(
    val applyId: Long,
    val period: String,
    val date: String,
    val testTime: String,
)

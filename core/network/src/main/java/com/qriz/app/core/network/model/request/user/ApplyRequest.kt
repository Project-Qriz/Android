package com.qriz.app.core.network.model.request.user

data class ApplyRequest(
    val applyId: Int,
    val startDate: String,
    val endDate: String,
    val examDate: String,
    val startTime: String,
    val endTime: String,
)

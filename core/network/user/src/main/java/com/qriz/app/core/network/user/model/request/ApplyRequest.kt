package com.qriz.app.core.network.user.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplyRequest(
    @SerialName("applyId") val applyId: Int,
    @SerialName("startDate") val startDate: String,
    @SerialName("endDate") val endDate: String,
    @SerialName("examDate") val examDate: String,
    @SerialName("startTime") val startTime: String,
    @SerialName("endTime") val endTime: String,
)
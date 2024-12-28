package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindIdResponse(
    @SerialName("applyId") val applyId: Long,
    @SerialName("period") val period: String,
    @SerialName("date") val date: String,
    @SerialName("testTime") val testTime: String
)
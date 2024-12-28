package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplyListResponse(
    @SerialName("applyId") val applyId: Long,
    @SerialName("examName") val examName :String,
    @SerialName("period") val period: String,
    @SerialName("examDate") val date: String,
    @SerialName("releaseDate") val releaseDate: String,
)
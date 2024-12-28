package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplyResponse(
    @SerialName("examName") val examName :String,
    @SerialName("period") val period: String,
    @SerialName("examDate") val examDate: String,
    @SerialName("releaseDate") val releaseDate: String,
)
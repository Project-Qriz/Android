package com.qriz.app.core.network.application.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DdayResponse(
    @SerialName("dday") val dday: Int,
    @SerialName("status") val status: String
)

package com.qriz.app.core.network.mock_test.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MockTestSessionResponse(
    @SerialName("completed") val completed: Boolean,
    @SerialName("session") val session: String,
    @SerialName("totalScore") val totalScore: Int?,
)

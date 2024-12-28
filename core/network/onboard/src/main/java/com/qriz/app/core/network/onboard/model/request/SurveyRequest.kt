package com.qriz.app.core.network.onboard.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurveyRequest(
    @SerialName("keyConcept") val keyConcept: List<String>
)
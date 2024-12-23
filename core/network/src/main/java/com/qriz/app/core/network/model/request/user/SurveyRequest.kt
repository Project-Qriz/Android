package com.qriz.app.core.network.model.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurveyRequest(
   @SerialName("keyConcept") val keyConcept: List<String>
)

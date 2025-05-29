package com.qriz.app.core.network.application.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationData(
    @SerialName("registeredApplicationId") val registeredApplicationId: Int,
    @SerialName("registeredUserApplyId") val registeredUserApplyId: Int?,
    @SerialName("applications") val applications: List<Application>
)

@Serializable
data class Application(
    @SerialName("applicationId") val applicationId: Int,
    @SerialName("userApplyId") val userApplyId: Int?,
    @SerialName("examName") val examName: String,
    @SerialName("period") val period: String,
    @SerialName("examDate") val examDate: String,
    @SerialName("releaseDate") val releaseDate: String
)

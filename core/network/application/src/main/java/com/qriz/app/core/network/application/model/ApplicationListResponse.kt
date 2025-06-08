package com.qriz.app.core.network.application.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExamListResponse(
    @SerialName("registeredApplicationId") val registeredApplicationId: Int?,
    @SerialName("registeredUserApplyId") val registeredUserApplyId: Int?,
    @SerialName("applications") val applications: List<ExamSchedule>
)

@Serializable
data class ExamSchedule(
    @SerialName("applicationId") val applicationId: Int,
    @SerialName("userApplyId") val userApplyId: Int?,
    @SerialName("examName") val examName: String,
    @SerialName("period") val period: String,
    @SerialName("examDate") val examDate: String,
    @SerialName("releaseDate") val releaseDate: String
)

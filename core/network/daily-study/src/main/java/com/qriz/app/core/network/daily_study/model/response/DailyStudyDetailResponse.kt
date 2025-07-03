package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyStudyDetailResponse(
    @SerialName("dayNumber") val dayNumber: String,
    @SerialName("skills") val skills: List<SimplePlannedSkillResponse>,
    @SerialName("status") val status: DailyStudyStatusResponse,
)

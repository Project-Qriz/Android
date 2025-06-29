package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyStudyPlanResponse(
    @SerialName("id") val id: Long,
    @SerialName("dayNumber") val dayNumber: String,
    @SerialName("completed") val completed: Boolean,
    @SerialName("planDate") val planDate: String,
    @SerialName("completionDate") val completionDate: String?,
    @SerialName("plannedSkills") val plannedSkills: List<PlannedSkillResponse>,
    @SerialName("reviewDay") val reviewDay: Boolean,
    @SerialName("comprehensiveReviewDay") val comprehensiveReviewDay: Boolean,
)

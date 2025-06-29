package com.qriz.app.core.network.daily_study.model.response

data class DailyStudyDetailResponse(
    val dayNumber: String,
    val skills: List<SimplePlannedSkillResponse>,
    val status: DailyStudyStatusResponse,
)

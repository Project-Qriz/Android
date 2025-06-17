package com.qriz.app.core.data.daily_study.daily_study_api.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlannedSkill(
    val id: Long,
    val type: String,
    val keyConcept: String,
    val description: String,
)

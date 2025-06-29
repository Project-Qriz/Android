package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlannedSkillResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("type")
    val type: String,
    
    @SerialName("keyConcept")
    val keyConcept: String,
    
    @SerialName("description")
    val description: String,
) 
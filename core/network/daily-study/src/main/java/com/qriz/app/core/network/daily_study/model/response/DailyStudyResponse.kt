package com.qriz.app.core.network.daily_study.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DailyTestQuestionResponse(
    val questionId: Long,
    val skillId: Int,
    val category: Int,
    val question: String,
    val description: String?,
    val options: List<DailyTestOptionResponse>,
    val timeLimit: Int,
    val difficulty: Int
)

@Serializable
data class DailyTestOptionResponse(
    val id: Long,
    val content: String
)

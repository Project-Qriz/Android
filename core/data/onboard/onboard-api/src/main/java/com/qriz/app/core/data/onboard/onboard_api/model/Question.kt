package com.qriz.app.core.data.onboard.onboard_api.model

data class Question(
    val id: Long,
    val question: String,
    val options: List<String>,
    val timeLimit: Int,
)
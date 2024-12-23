package com.qriz.app.core.data.model.test

data class Question(
    val id: Long,
    val question: String,
    val options: List<String>,
    val timeLimit: Int,
)

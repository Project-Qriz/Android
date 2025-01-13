package com.qriz.app.core.data.test.test_api.model

data class QuestionResult(
    val skillName: String,
    val question: String,
    val options: List<Option>,
    val answer: Option,
    val solution: String,
    val checked: Option,
    val correction: Boolean
)

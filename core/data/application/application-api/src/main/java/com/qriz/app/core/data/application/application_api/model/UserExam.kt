package com.qriz.app.core.data.application.application_api.model

data class UserExam(
    val examName: String,
    val period: String,
    val examDate: String,
    val dday: Int,
    val ddayType: DdayType,
)

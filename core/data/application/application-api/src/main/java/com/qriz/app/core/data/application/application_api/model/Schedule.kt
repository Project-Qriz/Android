package com.qriz.app.core.data.application.application_api.model

data class Schedule(
    val applicationId: Int,
    val userApplyId: Int?,
    val examName: String,
    val period: String,
    val examDate: String,
    val releaseDate: String
)

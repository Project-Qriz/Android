package com.qriz.app.core.data.application.application_api.model

data class Exam(
    val registeredApplicationId: Int,
    val registeredUserApplyId: Int?,
    val schedules: List<Schedule>
)

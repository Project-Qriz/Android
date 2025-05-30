package com.qriz.core.data.application.application.mapper

import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.network.application.model.Application
import com.qriz.app.core.network.application.model.ApplicationData

fun ApplicationData.toDomain(): List<Schedule> = applications.map { it.toDomain() }

private fun Application.toDomain(): Schedule = Schedule(
    applicationId = applicationId,
    userApplyId = userApplyId,
    examName = examName,
    period = period,
    examDate = examDate,
    releaseDate = releaseDate
)

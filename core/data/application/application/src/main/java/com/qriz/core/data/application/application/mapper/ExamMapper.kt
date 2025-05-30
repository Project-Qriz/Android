package com.qriz.core.data.application.application.mapper

import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.network.application.model.Application

fun Application.toSchedule(): Schedule {
    return Schedule(
        applicationId = applicationId,
        examDate = examDate,
        examName = examName,
        period = period,
        userApplyId = userApplyId,
        releaseDate = releaseDate,
    )
}

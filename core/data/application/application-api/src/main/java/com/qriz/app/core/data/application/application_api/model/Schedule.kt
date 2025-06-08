package com.qriz.app.core.data.application.application_api.model

import java.time.LocalDateTime
import java.time.ZoneId

data class Schedule(
    val applicationId: Int,
    val userApplyId: Int?,
    val examName: String,
    val period: String,
    val examDate: String,
    val releaseDate: String,
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
) {
    val periodStartEpochMilli: Long =
        periodStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val periodEndEpochMilli: Long =
        periodEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

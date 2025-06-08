package com.qriz.core.data.application.application.mapper

import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.network.application.model.ExamSchedule
import com.qriz.app.core.network.application.model.ExamListResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun ExamListResponse.toScheduleList(): List<Schedule> = applications.map { it.toSchedule() }

private fun ExamSchedule.toSchedule(): Schedule {
    val (startPart, endPart) = period.split(" ~ ")
    
    val currentYear = LocalDateTime.now().year
    
    fun parsePart(part: String): LocalDateTime {
        val dateTimeWithYear = "$currentYear.$part"
        val formatter = DateTimeFormatter.ofPattern(
            "yyyy.MM.dd(E) HH:mm",
            Locale.KOREAN
        )
        return LocalDateTime.parse(
            dateTimeWithYear,
            formatter
        )
    }

    val periodStart = parsePart(startPart.trim())
    val periodEnd = parsePart(endPart.trim())
    
    return Schedule(
        applicationId = applicationId,
        userApplyId = userApplyId,
        examName = examName,
        period = period,
        examDate = examDate,
        releaseDate = releaseDate,
        periodStart = periodStart,
        periodEnd = periodEnd
    )
}

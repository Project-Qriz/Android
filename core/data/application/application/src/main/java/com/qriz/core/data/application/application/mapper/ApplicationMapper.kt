package com.qriz.core.data.application.application.mapper

import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.network.application.model.ExamSchedule
import com.qriz.app.core.network.application.model.ExamListResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun ExamListResponse.toScheduleList(): List<Schedule> = applications.map { it.toSchedule() }

private fun ExamSchedule.toSchedule(): Schedule {
    val input = period.split(" ~ ")[1]
    val parts = input.split(" ")
    val datePart = parts[0].removeSuffix("(금)").replace(".", "-")
    val timePart = parts[1]
    val currentYear = LocalDateTime.now().year
    val fullDate = "$currentYear-$datePart $timePart"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val targetDate = LocalDateTime.parse(fullDate, formatter)

    return Schedule(
        applicationId = applicationId,
        userApplyId = userApplyId,
        examName = examName,
        period = period,
        examDate = examDate,
        releaseDate = releaseDate,
        applicationDeadline = targetDate
    )
}

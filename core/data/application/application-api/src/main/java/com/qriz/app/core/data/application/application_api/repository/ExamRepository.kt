package com.qriz.app.core.data.application.application_api.repository

import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface ExamRepository {
    fun getUserExams(): Flow<ApiResult<UserExam?>>

    suspend fun getExamSchedules(): ApiResult<List<Schedule>>
}

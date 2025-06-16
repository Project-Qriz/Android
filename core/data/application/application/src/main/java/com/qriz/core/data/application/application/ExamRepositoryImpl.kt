package com.qriz.core.data.application.application

import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.data.application.application_api.model.UserExam
import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.flatMapSuspend
import com.qriz.app.core.model.map
import com.qriz.app.core.network.application.api.ApplicationApi
import com.qriz.app.core.network.application.model.request.ApplicationModifyRequest
import com.qriz.app.core.network.application.model.request.ApplicationRequest
import com.qriz.core.data.application.application.mapper.toScheduleList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ExamRepositoryImpl @Inject constructor(
    private val applicationApi: ApplicationApi,
) : ExamRepository {
    private var initialized = false
    private val cachedUserExam = MutableStateFlow<ApiResult<UserExam?>>(ApiResult.Success(null))

    private lateinit var cachedExamSchedules: List<Schedule>

    override fun getUserExams(): Flow<ApiResult<UserExam?>> = cachedUserExam.onStart {
        if (initialized.not()) {
            fetchUserExam()
            initialized = true
        }
    }

    override suspend fun getExamSchedules(): ApiResult<List<Schedule>> {
        if (::cachedExamSchedules.isInitialized) {
            return ApiResult.Success(cachedExamSchedules)
        }

        return applicationApi.applications().map { response ->
            response.toScheduleList().also { cachedExamSchedules = it }
        }
    }

    override suspend fun applyExam(examId: Long): ApiResult<Unit> {
        return applicationApi
            .apply(request = ApplicationRequest(applyId = examId))
            .map { fetchUserExam() }
    }

    override suspend fun editExam(uaid: Long, examId: Long): ApiResult<Unit> {
        return applicationApi
            .modify(id = uaid, request = ApplicationModifyRequest(newApplyId = examId))
            .map { fetchUserExam() }
    }

    private suspend fun fetchUserExam() {
        val applicationResult = applicationApi.getUserApplicationInfo()
        if (applicationResult is ApiResult.Failure) {
            cachedUserExam.update { ApiResult.Success(null) }
            return
        }

        val result = applicationResult.flatMapSuspend { application ->
            applicationApi.getDday().map { ddayResponse ->
                UserExam(
                    examName = application.examName,
                    period = application.period,
                    examDate = application.examDate,
                    dday = ddayResponse.dday,
                    ddayType = DdayType.fromStatus(ddayResponse.status.uppercase())
                )
            }
        }
        cachedUserExam.update { result }
    }
}

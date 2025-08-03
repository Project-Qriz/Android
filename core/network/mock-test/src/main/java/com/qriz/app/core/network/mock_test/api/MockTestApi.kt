package com.qriz.app.core.network.mock_test.api

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.mock_test.model.request.MockTestSubmitRequest
import com.qriz.app.core.network.mock_test.model.response.MockTestQuestionsResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestResultResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestScoreResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MockTestApi {
    @GET("/api/v1/exam/get/{examId}")
    suspend fun getMockTestQuestions(
        @Path("examId") id: Long,
    ): ApiResult<MockTestQuestionsResponse>

    @GET("/api/v1/exam/session-list")
    suspend fun getMockTestSessions(
        @Query("status") completed: Boolean?
    ): ApiResult<List<MockTestSessionResponse>>

    @POST("/api/v1/exam/submit/{examId}")
    suspend fun submitMockTest(
        @Path("examId") id: Long,
        @Body request: MockTestSubmitRequest
    ): ApiResult<Unit>

    @GET("/api/v1/exam/{examId}/results")
    suspend fun getMockTestResult(
        @Path("examId") id: Long
    ): ApiResult<MockTestResultResponse>

    @GET("/api/v1/exam/{examId}/subject-details")
    suspend fun getMockTestSubjectDetails(
        @Path("examId") id: Long,
    ): ApiResult<List<MockTestScoreResponse>>
}

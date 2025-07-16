package com.qriz.app.core.network.mock_test.model.api

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.mock_test.model.response.MockTestQuestionsResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestSessionResponse
import retrofit2.http.GET
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
}

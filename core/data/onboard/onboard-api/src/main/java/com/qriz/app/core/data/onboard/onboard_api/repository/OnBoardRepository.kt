package com.qriz.app.core.data.onboard.onboard_api.repository

import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.model.ApiResult

interface OnBoardRepository {
    suspend fun submitSurvey(concepts: Collection<SQLDConcept>): ApiResult<Unit>

    suspend fun getPreviewTest(): Test

    suspend fun submitPreviewTest(answer: Map<Long, Option>)
    suspend fun getPreviewTestResult(): PreviewTestResult
}

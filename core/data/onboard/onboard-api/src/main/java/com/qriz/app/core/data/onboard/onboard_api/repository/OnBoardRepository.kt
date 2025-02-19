package com.qriz.app.core.data.onboard.onboard_api.repository

import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test

interface OnBoardRepository {
    fun submitSurvey(concepts: Collection<PreCheckConcept>)

    suspend fun getPreviewTest(): Test

    suspend fun submitPreviewTest(answer: Map<Long, Option>)
    suspend fun getPreviewTestResult(): PreviewTestResult
}

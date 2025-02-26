package com.qriz.app.core.data.onboard.onboard.repository

import com.qriz.app.core.data.onboard.onboard.mapper.toPreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.data.test.test_api.model.TestCategory
import com.qriz.app.core.network.common.util.verifyResponseCode
import com.qriz.app.core.network.onboard.api.OnBoardApi
import com.qriz.app.core.data.onboard.onboard.mapper.toTest
import com.qriz.app.core.network.onboard.model.request.SurveyRequest
import com.qriz.app.core.network.onboard.model.request.TestSubmitActivity
import com.qriz.app.core.network.onboard.model.request.TestSubmitQuestion
import com.qriz.app.core.network.onboard.model.request.TestSubmitRequest
import javax.inject.Inject

internal class OnBoardRepositoryImpl @Inject constructor(
    private val onBoardApi: OnBoardApi
) : OnBoardRepository {

    override fun submitSurvey(concepts: Collection<SQLDConcept>) {
        onBoardApi.submitSurvey(
            SurveyRequest(keyConcept = concepts.map { it.title })
        ).verifyResponseCode()
    }

    override suspend fun getPreviewTest(): Test {
        val response = onBoardApi.getPreviewTest().verifyResponseCode()
        return response.data.toTest()
    }

    override suspend fun submitPreviewTest(answer: Map<Long, Option>) {
        val request = TestSubmitRequest(
            activities = answer.map { (questionId, option) ->
                TestSubmitActivity(
                    question = TestSubmitQuestion(
                        questionId = questionId,
                        category = TestCategory.PREVIEW.id
                    ),
                    questionNum = questionId.toInt(), //TODO: 서버 수정 대기
                    checked = option.description
                )
            }
        )
        onBoardApi.submitPreviewTest(request).verifyResponseCode()
    }

    override suspend fun getPreviewTestResult(): PreviewTestResult {
        val response = onBoardApi.getPreviewTestResult().verifyResponseCode()
        return response.data.toPreviewTestResult()
    }

}

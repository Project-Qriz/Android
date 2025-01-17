package com.qriz.app.core.data.onboard.onboard.repository

import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.data.test.test_api.model.TestCategory
import com.qriz.app.core.network.onboard.api.OnBoardApi
import com.qriz.app.core.network.onboard.mapper.toPreviewTestResult
import com.qriz.app.core.network.onboard.mapper.toTest
import com.qriz.app.core.network.onboard.model.request.SurveyRequest
import com.qriz.app.core.network.onboard.model.request.TestSubmitActivity
import com.qriz.app.core.network.onboard.model.request.TestSubmitQuestion
import com.qriz.app.core.network.onboard.model.request.TestSubmitRequest
import javax.inject.Inject

internal class OnBoardRepositoryImpl @Inject constructor(
    private val onBoardApi: OnBoardApi
) : OnBoardRepository {

    override fun submitSurvey(concepts: Collection<PreCheckConcept>) {
        val response = onBoardApi.submitSurvey(
            SurveyRequest(keyConcept = concepts.map { it.title })
        )
        if (response.isFailure) throw Exception(response.message)
    }

    override suspend fun getPreviewTest(): Test {
        val response = onBoardApi.getPreviewTest()
        if (response.isFailure) throw Exception(response.message)
        return onBoardApi.getPreviewTest().data.toTest()
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
        val response = onBoardApi.submitPreviewTest(request)
        if (response.isFailure) throw Exception(response.message)
    }

    override suspend fun getPreviewTestResult(): PreviewTestResult {
        val response = onBoardApi.getPreviewTestResult()
        if (response.isFailure) throw Exception(response.message)
        return response.data.toPreviewTestResult()
    }

}

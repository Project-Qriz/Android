package com.qriz.app.core.data.onboard.onboard.repository

import com.qriz.app.core.data.onboard.onboard.mapper.toPreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.data.test.test_api.model.TestCategory
import com.qriz.app.core.network.onboard.api.OnBoardApi
import com.qriz.app.core.data.onboard.onboard.mapper.toTest
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.map
import com.qriz.app.core.network.onboard.model.request.SurveyRequest
import com.qriz.app.core.network.onboard.model.request.TestSubmitActivity
import com.qriz.app.core.network.onboard.model.request.TestSubmitQuestion
import com.qriz.app.core.network.onboard.model.request.TestSubmitRequest
import javax.inject.Inject

internal class OnBoardRepositoryImpl @Inject constructor(
    private val onBoardApi: OnBoardApi
) : OnBoardRepository {

    override suspend fun submitSurvey(concepts: Collection<SQLDConcept>): ApiResult<Unit> {
        return onBoardApi.submitSurvey(
            SurveyRequest(keyConcepts = concepts.map { it.title })
        )
    }

    override suspend fun getPreviewTest(): ApiResult<Test> {
        val response = onBoardApi.getPreviewTest()
        return response.map { it.toTest() }
    }

    override suspend fun submitPreviewTest(answer: Map<Long, Option>): ApiResult<Unit> {
        val request = TestSubmitRequest(
            activities = answer.toList().mapIndexed { index, (questionId, option) ->
                TestSubmitActivity(
                    question = TestSubmitQuestion(
                        questionId = questionId,
                        category = TestCategory.PREVIEW.id
                    ),
                    questionNum = index + 1,
                    optionId = option.id,
                )
            }
        )

        return onBoardApi.submitPreviewTest(request)
    }

    override suspend fun getPreviewTestResult(): ApiResult<PreviewTestResult> {
        return onBoardApi.getPreviewTestResult().map { it.toPreviewTestResult() }
    }
}

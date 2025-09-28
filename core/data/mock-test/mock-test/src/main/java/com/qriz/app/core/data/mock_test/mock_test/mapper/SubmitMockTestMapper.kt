package com.qriz.app.core.data.mock_test.mock_test.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.TestCategory
import com.qriz.app.core.network.mock_test.model.request.MockTestSubmitActivity
import com.qriz.app.core.network.mock_test.model.request.MockTestSubmitQuestion
import com.qriz.app.core.network.mock_test.model.request.MockTestSubmitRequest

fun Map<Long, Option>.toRequest(): MockTestSubmitRequest = MockTestSubmitRequest(
    activities = toList().mapIndexed { index, (questionId, option) ->
        MockTestSubmitActivity(
            question = MockTestSubmitQuestion(
                questionId = questionId,
                category = TestCategory.PREVIEW.id
            ),
            questionNum = index + 1,
            optionId = option.id,
        )
    }
)

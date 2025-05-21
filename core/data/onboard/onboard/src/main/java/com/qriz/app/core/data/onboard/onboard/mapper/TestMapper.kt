package com.qriz.app.core.data.onboard.onboard.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.network.onboard.model.response.PreviewTestListResponse
import com.qriz.app.core.network.onboard.model.response.QuestionOptionModel
import com.qriz.app.core.network.onboard.model.response.QuestionResponseModel

fun PreviewTestListResponse.toTest() = Test(
    questions = questions.map(QuestionResponseModel::toQuestion),
    totalTimeLimit = totalTimeLimit
)

fun QuestionResponseModel.toQuestion() =
    Question(
        id = questionId,
        question = question,
        options = options.map(QuestionOptionModel::toOption),
        timeLimit = timeLimit,
        category = category,
        description = description,
        difficulty = difficulty,
    )

fun QuestionOptionModel.toOption() =
    Option(
        id = id,
        content = content,
    )

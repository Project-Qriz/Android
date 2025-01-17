package com.qriz.app.core.network.onboard.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.network.onboard.model.response.PreviewTestListResponse
import com.qriz.app.core.network.onboard.model.response.QuestionResponseModel

fun PreviewTestListResponse.toTest() = Test(
    questions = questions.map(QuestionResponseModel::toQuestion),
    totalTimeLimit = totalTimeLimit
)

fun QuestionResponseModel.toQuestion() =
    Question(
        id = question.hashCode().toLong(), //TODO : API 수정 대기 (문서에 ID 포함되어있지 않음)
        question = question,
        options = listOf(
            Option(option1),
            Option(option2),
            Option(option3),
            Option(option4),
        ),
        timeLimit = timeLimit,
    )

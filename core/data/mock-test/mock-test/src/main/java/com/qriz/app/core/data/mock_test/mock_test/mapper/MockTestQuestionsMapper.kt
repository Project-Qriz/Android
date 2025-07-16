package com.qriz.app.core.data.mock_test.mock_test.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.network.mock_test.model.response.MockTestOptionResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestQuestionResponse
import com.qriz.app.core.network.mock_test.model.response.MockTestQuestionsResponse

fun MockTestQuestionsResponse.toTest(): Test {
    return Test(
        questions = questions.map { it.toQuestion() },
        totalTimeLimit = totalTimeLimit
    )
}

private fun MockTestQuestionResponse.toQuestion(): Question {
    return Question(
        id = questionId,
        question = question,
        options = options.map { it.toOption() },
        timeLimit = timeLimit,
        description = description,
        skillId = skillId.toInt(),
        category = category,
        difficulty = difficulty
    )
}

private fun MockTestOptionResponse.toOption(): Option {
    return Option(
        id = id,
        content = content
    )
}

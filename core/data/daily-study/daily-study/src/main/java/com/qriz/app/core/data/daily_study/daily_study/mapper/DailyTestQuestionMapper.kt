package com.qriz.app.core.data.daily_study.daily_study.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.network.daily_study.model.response.DailyTestOptionResponse
import com.qriz.app.core.network.daily_study.model.response.DailyTestQuestionResponse

internal fun List<DailyTestQuestionResponse>.toTest() = Test(
    questions = map { it.toQuestion() },
    totalTimeLimit = sumOf { it.timeLimit }
)

private fun DailyTestQuestionResponse.toQuestion() = Question(
    id = questionId,
    question = question,
    options = options.map { it.toOption() },
    timeLimit = timeLimit,
    description = description,
    skillId = skillId,
    category = category,
    difficulty = difficulty
)

private fun DailyTestOptionResponse.toOption() = Option(
    id = id,
    content = content
)

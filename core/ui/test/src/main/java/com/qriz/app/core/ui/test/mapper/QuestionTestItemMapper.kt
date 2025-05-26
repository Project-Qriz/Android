package com.qriz.app.core.ui.test.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import kotlinx.collections.immutable.toImmutableList

fun List<Question>.toQuestionTestItem(isSelectedOptionMap: Map<Long, Option>): List<QuestionTestItem> {
    return map { question -> question.toQuestionTestItem(isSelectedOptionMap) }
}

fun Question.toQuestionTestItem(isSelectedOptionMap: Map<Long, Option>) =
    QuestionTestItem(
        id = id,
        question = question,
        timeLimit = timeLimit,
        description = description,
        isOptionSelected = isSelectedOptionMap[id] != null,
        options = options.map { option ->
            val isSelected = isSelectedOptionMap[this.id]?.content == option.content
            when {
                isSelected -> SelectedOrCorrectOptionItem(option.id, option.content)
                else -> GeneralOptionItem(option.id, option.content)
            }
        }.toImmutableList(),
    )

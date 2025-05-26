package com.qriz.app.core.ui.test.mapper

import com.qriz.app.core.data.test.test_api.model.QuestionResult
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.QuestionResultItem
import com.qriz.app.core.ui.test.model.SelectedAndIncorrectOptionItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem
import kotlinx.collections.immutable.toImmutableList

fun List<QuestionResult>.toQuestionResultItem(): List<QuestionResultItem> {
    return map { question -> question.toQuestionResultItem() }
}

fun QuestionResult.toQuestionResultItem(): QuestionResultItem {
    return QuestionResultItem(
        skillName = skillName,
        question = question,
        solution = solution,
        correction = correction,
        options = options.map { option ->
            val isSelected = option == checked
            val isCorrect = option == answer
            val isWrongChoice = isSelected.not() && isCorrect
            val isGoodChoice = isSelected && isCorrect
            when {
                isWrongChoice -> SelectedAndIncorrectOptionItem(option.id, option.content)
                isGoodChoice || isSelected -> SelectedOrCorrectOptionItem(option.id, option.content)
                else -> GeneralOptionItem(option.id, option.content)
            }
        }.toImmutableList()
    )
}

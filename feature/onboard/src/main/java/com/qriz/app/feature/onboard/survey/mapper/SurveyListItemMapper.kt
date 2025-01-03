package com.qriz.app.feature.onboard.survey.mapper

import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.feature.onboard.survey.model.SurveyListItem

fun List<PreCheckConcept>.toSurveyListItem(
    isCheckedMap: Map<PreCheckConcept, Boolean>,
): List<SurveyListItem> {
    val checkedCount = isCheckedMap.values.filter { it }.size
    val unCheckedCount = isCheckedMap.values.filter { it.not() }.size

    val isAllChecked = checkedCount == PreCheckConcept.entries.size
    val isAllUnChecked = unCheckedCount == PreCheckConcept.entries.size

    return listOf(
        SurveyListItem.KnowsNothing(isChecked = isAllUnChecked),
        SurveyListItem.KnowsAll(isChecked = isAllChecked)
    ) + this.map { concept ->
        SurveyListItem.SurveyItem(
            concept = concept,
            isChecked = isCheckedMap[concept] ?: false
        )
    }
}


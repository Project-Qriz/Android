package com.qriz.app.feature.onboard.survey.mapper

import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.feature.onboard.survey.model.SurveyListItem
import kotlinx.collections.immutable.toImmutableList

fun List<SQLDConcept>.toSurveyListItem(
    isCheckedMap: Map<SQLDConcept, Boolean>,
): List<SurveyListItem> {
    val checkedCount = isCheckedMap.values.filter { it }.size
    val unCheckedCount = isCheckedMap.values.filter { it.not() }.size

    val isAllChecked = checkedCount == SQLDConcept.entries.size
    val isAllUnChecked = unCheckedCount == SQLDConcept.entries.size

    return listOf(
        SurveyListItem.KnowsNothing(isChecked = isAllUnChecked),
        SurveyListItem.KnowsAll(isChecked = isAllChecked),
        SurveyListItem.SurveyItemGroup(
            list = this.map { concept ->
                SurveyListItem.SurveyItem(
                    concept = concept,
                    isChecked = isCheckedMap[concept] ?: false
                )
            }.toImmutableList(),
        ),
    )
}


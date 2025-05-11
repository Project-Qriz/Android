package com.qriz.app.feature.onboard.survey.model

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed class SurveyListItem(
    open val isChecked: Boolean
) {
    fun getCategoryId(): Int {
        return when (this) {
            is KnowsNothing -> KNOWS_NOTHING_ITEM_STABLE_ID
            is KnowsAll -> KNOWS_ALL_ITEM_STABLE_ID
            is SurveyItemGroup -> SURVEY_ITEM_GROUP_ID
        }
    }

    @Immutable
    data class KnowsNothing(
        override val isChecked: Boolean
    ) : SurveyListItem(isChecked)

    @Immutable
    data class KnowsAll(
        override val isChecked: Boolean
    ) : SurveyListItem(isChecked)

    @Immutable
    data class SurveyItemGroup(
        override val isChecked: Boolean = false,
        val list: ImmutableList<SurveyItem>
    ) : SurveyListItem(isChecked)

    @Immutable
    data class SurveyItem(
        val concept: SQLDConcept,
        val isChecked: Boolean
    )

    companion object {
        const val KNOWS_NOTHING_ITEM_STABLE_ID = -1
        const val KNOWS_ALL_ITEM_STABLE_ID = -2
        const val SURVEY_ITEM_GROUP_ID = -3
    }
}

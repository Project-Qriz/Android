package com.qriz.app.feature.onboard.ui.screen.survey.model

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept

@Immutable
sealed class SurveyListItem(
    open val isChecked: Boolean
) {
    fun getCategoryId(): Int {
        return when (this) {
            is KnowsNothing -> KNOWS_NOTHING_ITEM_STABLE_ID
            is KnowsAll -> KNOWS_ALL_ITEM_STABLE_ID
            is SurveyItem -> concept.hashCode()
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
    data class SurveyItem(
        val concept: PreCheckConcept,
        override val isChecked: Boolean
    ) : SurveyListItem(isChecked)

    companion object {
        const val KNOWS_NOTHING_ITEM_STABLE_ID = -1
        const val KNOWS_ALL_ITEM_STABLE_ID = -2
    }
}

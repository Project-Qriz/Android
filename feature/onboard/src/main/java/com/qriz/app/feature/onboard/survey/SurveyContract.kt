package com.qriz.app.feature.onboard.survey

import androidx.compose.runtime.Immutable
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.onboard.survey.model.SurveyListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SurveyUiState(
    val surveyItems: ImmutableList<SurveyListItem>,
) : UiState {

    val isPossibleSubmit
        get() = surveyItems.any { it.isChecked }

    companion object {
        val Default = SurveyUiState(
            surveyItems = persistentListOf(),
        )
    }
}

sealed interface SurveyUiAction : UiAction {
    data object ObserveSurveyItems : SurveyUiAction
    data class ClickKnowsAll(val isChecked: Boolean) : SurveyUiAction
    data class ClickKnowsNothing(val isChecked: Boolean) : SurveyUiAction
    data class ClickConcept(
        val sqldConcept: SQLDConcept,
        val isChecked: Boolean
    ) : SurveyUiAction

    data object ClickSubmit : SurveyUiAction
}

sealed interface SurveyUiEffect : UiEffect {
    data object MoveToGuide : SurveyUiEffect
    data class ShowSnackBar(val message: String) : SurveyUiEffect
}

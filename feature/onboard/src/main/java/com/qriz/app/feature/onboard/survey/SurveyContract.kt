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
    val isExpandSurveyItemGroup: Boolean,
    val showNetworkErrorDialog: Boolean,
    val showErrorDialog: Boolean,
) : UiState {

    val isPossibleSubmit
        get() = surveyItems.any {
            if (it is SurveyListItem.SurveyItemGroup) {
                it.list.any { item -> item.isChecked }
            } else {
                it.isChecked
            }
        }

    companion object {
        val Default = SurveyUiState(
            surveyItems = persistentListOf(),
            isExpandSurveyItemGroup = true,
            showNetworkErrorDialog = false,
            showErrorDialog = false,
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
    data class ChangeExpandSurveyItemGroup(val isExpand: Boolean) : SurveyUiAction
    data object ConfirmNetworkErrorDialog : SurveyUiAction
    data object ConfirmErrorDialog : SurveyUiAction
}

sealed interface SurveyUiEffect : UiEffect {
    data object MoveToGuide : SurveyUiEffect
    data class ShowSnackBar(val message: String) : SurveyUiEffect
}

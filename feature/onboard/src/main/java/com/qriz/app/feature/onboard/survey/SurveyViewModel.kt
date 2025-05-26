package com.qriz.app.feature.onboard.survey

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.onboard.survey.mapper.toSurveyListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val onBoardRepository: OnBoardRepository
) : BaseViewModel<SurveyUiState, SurveyUiEffect, SurveyUiAction>(SurveyUiState.Default) {
    private val isTest = savedStateHandle.get<Boolean>(IS_TEST_FLAG) ?: false

    private val _isChecked = MutableStateFlow<Map<SQLDConcept, Boolean>>(emptyMap())

    init {
        if (isTest.not()) process(SurveyUiAction.ObserveSurveyItems)
    }

    override fun process(action: SurveyUiAction) = viewModelScope.launch {
        when (action) {
            SurveyUiAction.ObserveSurveyItems -> observeSurveyListItems()
            is SurveyUiAction.ClickKnowsAll -> onClickKnowsAll(action.isChecked)
            is SurveyUiAction.ClickKnowsNothing -> onClickKnowsNothing(action.isChecked)
            is SurveyUiAction.ClickConcept -> onClickConcept(
                sqldConcept = action.sqldConcept,
                isChecked = action.isChecked
            )

            is SurveyUiAction.ClickSubmit -> onClickSubmit()
            is SurveyUiAction.ChangeExpandSurveyItemGroup -> updateState {
                copy(isExpandSurveyItemGroup = action.isExpand)
            }

            is SurveyUiAction.ConfirmErrorDialog -> { updateState { copy(showErrorDialog = false) } }
            is SurveyUiAction.ConfirmNetworkErrorDialog -> { updateState { copy(showNetworkErrorDialog = false) } }
        }
    }

    private fun observeSurveyListItems() = viewModelScope.launch {
        _isChecked.collect { isCheckedMap ->
            updateState {
                copy(
                    surveyItems = SQLDConcept.entries
                        .toSurveyListItem(isCheckedMap)
                        .toImmutableList()
                )
            }
        }
    }

    private fun onClickKnowsAll(isChecked: Boolean) {
        if (isChecked) _isChecked.update { SQLDConcept.entries.associateWith { true } }
        else _isChecked.update { emptyMap() }
    }

    private fun onClickKnowsNothing(isChecked: Boolean) {
        if (isChecked) _isChecked.update { SQLDConcept.entries.associateWith { false } }
        else _isChecked.update { emptyMap() }
    }

    private fun onClickConcept(sqldConcept: SQLDConcept, isChecked: Boolean) {
        if (isChecked) _isChecked.update { _isChecked.value + (sqldConcept to true) }
        else _isChecked.update { _isChecked.value - sqldConcept }
    }

    private fun onClickSubmit() {
        if (uiState.value.isPossibleSubmit.not()) return
        val checked = _isChecked.value.filter { it.value }.keys.toList()
        submitSurvey(checked)
    }

    private fun submitSurvey(checked: List<SQLDConcept>) = viewModelScope.launch {
        when(onBoardRepository.submitSurvey(checked)) {
            is ApiResult.Success -> {
                sendEffect(SurveyUiEffect.MoveToGuide)
            }

            is ApiResult.NetworkError -> {
                updateState { copy(showNetworkErrorDialog = true) }
            }

            is ApiResult.Failure,
            is ApiResult.UnknownError -> {
                updateState { copy(showErrorDialog = true) }
            }
        }
    }

    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
    }
}

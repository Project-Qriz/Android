package com.qriz.app.feature.onboard.survey

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.onboard.survey.mapper.toSurveyListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO : Timber 로그 찍어보자
//TODO : String Resources 정리
@HiltViewModel
class SurveyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val onBoardRepository: OnBoardRepository
) : BaseViewModel<SurveyUiState, SurveyUiEffect, SurveyUiAction>(SurveyUiState.Default) {
    private val isTest = savedStateHandle.get<Boolean>(IS_TEST_FLAG) ?: false

    private val _isChecked = MutableStateFlow<Map<PreCheckConcept, Boolean>>(emptyMap())

    init {
        if (isTest.not()) process(SurveyUiAction.ObserveSurveyItems)
    }

    override fun process(action: SurveyUiAction) = viewModelScope.launch {
        when (action) {
            SurveyUiAction.ObserveSurveyItems -> observeSurveyListItems()
            is SurveyUiAction.ClickKnowsAll -> onClickKnowsAll(action.isChecked)
            is SurveyUiAction.ClickKnowsNothing -> onClickKnowsNothing(action.isChecked)
            is SurveyUiAction.ClickConcept -> onClickConcept(
                preCheckConcept = action.preCheckConcept,
                isChecked = action.isChecked
            )

            is SurveyUiAction.ClickSubmit -> onClickSubmit()
        }
    }

    private fun observeSurveyListItems() = viewModelScope.launch {
        _isChecked.collect { isCheckedMap ->
            updateState {
                copy(
                    surveyItems = PreCheckConcept.entries
                        .toSurveyListItem(isCheckedMap)
                        .toImmutableList()
                )
            }
        }
    }

    private fun onClickKnowsAll(isChecked: Boolean) {
        if (isChecked) _isChecked.update { PreCheckConcept.entries.associateWith { true } }
        else _isChecked.update { emptyMap() }
    }

    private fun onClickKnowsNothing(isChecked: Boolean) {
        if (isChecked) _isChecked.update { PreCheckConcept.entries.associateWith { false } }
        else _isChecked.update { emptyMap() }
    }

    private fun onClickConcept(preCheckConcept: PreCheckConcept, isChecked: Boolean) {
        if (isChecked) _isChecked.update { _isChecked.value + (preCheckConcept to true) }
        else _isChecked.update { _isChecked.value - preCheckConcept }
    }

    private fun onClickSubmit() {
        if (uiState.value.isPossibleSubmit.not()) return
        val checked = _isChecked.value.filter { it.value }.keys.toList()
        submitSurvey(checked)
    }

    private fun submitSurvey(checked: List<PreCheckConcept>) = viewModelScope.launch {
        runCatching { onBoardRepository.submitSurvey(checked) }
            .onSuccess { sendEffect(SurveyUiEffect.MoveToGuide) }
            .onFailure { throwable ->
                sendEffect(
                    SurveyUiEffect.ShowSnackBar(
                        throwable.message ?: "알 수 없는 에러가 발생했습니다."
                    )
                )
            }
    }

    companion object {
        internal const val IS_TEST_FLAG = "IS_TEST_FLAG"
    }
}

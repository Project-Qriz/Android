package com.qriz.app.feature.home

import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeUiState, HomeUiEffect, HomeUiAction>(HomeUiState.Default) {

    override fun process(action: HomeUiAction): Job = viewModelScope.launch {
        when (action) {
            is HomeUiAction.ChangeTodayStudyCard -> onChangeTodayStudyCard(action.day)
        }
    }

    private fun onChangeTodayStudyCard(day: Int) {
        updateState { copy(currentTodayStudyDay = day) }
    }
}

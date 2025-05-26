package com.qriz.app.feature.home

import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<HomeUiState, HomeUiEffect, HomeUiAction>(HomeUiState.Default) {

    override fun process(action: HomeUiAction): Job = viewModelScope.launch {
        when (action) {
            is HomeUiAction.ObserveClient -> observeClient()
            is HomeUiAction.ChangeTodayStudyCard -> onChangeTodayStudyCard(action.day)
            is HomeUiAction.ClickTestDateRegister -> onClickTestDateRegister()
            is HomeUiAction.DismissTestDateBottomSheet -> onDismissTestDateBottomSheet()
            is HomeUiAction.MoveToPreviewTest -> sendEffect(HomeUiEffect.MoveToPreviewTest)
        }
    }

    private fun onChangeTodayStudyCard(day: Int) {
        updateState { copy(currentTodayStudyDay = day) }
    }

    private fun observeClient() = viewModelScope.launch {
        userRepository.getUserFlow().collect { user ->
            updateState { copy(user = user) }
        }
    }

    private fun onClickTestDateRegister() {
        updateState { copy(isShowTestDateBottomSheet = true) }
    }

    private fun onDismissTestDateBottomSheet() {
        updateState { copy(isShowTestDateBottomSheet = false) }
    }
}

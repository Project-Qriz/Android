package com.qriz.app.feature.mypage.setting

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.model.requireValue
import com.qriz.app.feature.base.BaseViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel<SettingUiState, SettingUiEffect, SettingUiAction>(
    SettingUiState.Default
) {

    override fun process(action: SettingUiAction): Job = viewModelScope.launch {
        when (action) {
            is SettingUiAction.LoadData -> loadData()
            is SettingUiAction.ClickResetPassword -> sendEffect(SettingUiEffect.NavigateToResetPassword)
            is SettingUiAction.ClickLogout -> updateState { copy(showLogoutDialog = true) }
            is SettingUiAction.ShowLogoutDialog -> updateState { copy(showLogoutDialog = true) }
            is SettingUiAction.DismissLogoutDialog -> updateState { copy(showLogoutDialog = false) }
            is SettingUiAction.ConfirmLogout -> logout()
            is SettingUiAction.ClickWithdraw -> sendEffect(SettingUiEffect.NavigateToWithdraw)
        }
    }

    private suspend fun logout() {
        updateState { copy(showLogoutDialog = false) }
        userRepository.logout()
        sendEffect(SettingUiEffect.NavigateToLogin)
    }

    private suspend fun loadData() {
        val user = userRepository.getUser().requireValue
        updateState {
            copy(
                user = user
            )
        }
    }
}

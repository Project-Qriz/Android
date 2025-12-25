package com.qriz.app.feature.mypage.setting

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.quiz.app.core.data.user.user_api.model.User

@Immutable
data class SettingUiState(
    val isLoading: Boolean,
    val user: User,
    val showLogoutDialog: Boolean,
) : UiState {

    companion object {
        val Default = SettingUiState(
            isLoading = false,
            user = User.Default,
            showLogoutDialog = false,
        )
    }
}

sealed interface SettingUiAction : UiAction {
    data object LoadData : SettingUiAction
    data object ClickResetPassword : SettingUiAction
    data object ClickLogout : SettingUiAction
    data object ShowLogoutDialog : SettingUiAction
    data object DismissLogoutDialog : SettingUiAction
    data object ConfirmLogout : SettingUiAction
    data object ClickWithdraw : SettingUiAction
}

sealed interface SettingUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : SettingUiEffect

    data object NavigateToResetPassword : SettingUiEffect
    data object NavigateToLogin : SettingUiEffect
    data object NavigateToWithdraw : SettingUiEffect
    data object ClearGoogleCredentials : SettingUiEffect
}

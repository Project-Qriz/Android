package com.qriz.app.feature.mypage.withdraw

import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class WithdrawUiState(
    val isLoading: Boolean,
    val showConfirmDialog: Boolean = false,
) : UiState {

    companion object {
        val Default = WithdrawUiState(
            isLoading = false,
            showConfirmDialog = false,
        )
    }
}

sealed interface WithdrawUiAction : UiAction {
    data object ClickWithdraw : WithdrawUiAction
    data object ConfirmWithdraw : WithdrawUiAction
    data object DismissConfirmDialog : WithdrawUiAction
}

sealed interface WithdrawUiEffect : UiEffect {
    data class ShowSnackBar(val message: String) : WithdrawUiEffect

    data object NavigateToLogin : WithdrawUiEffect
}

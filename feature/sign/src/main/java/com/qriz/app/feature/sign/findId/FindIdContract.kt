package com.qriz.app.feature.sign.findId

import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.model.EMAIL_REGEX

data class FindIdUiState(
    val email: String,
    val errorMessageResId: Int,
    val isVisibleSuccessDialog: Boolean,
    val isVisibleNetworkErrorDialog: Boolean,
    val errorDialogMessage: String?,
) : UiState {

    val isValidEmailFormat = EMAIL_REGEX.matches(email)

    companion object {
        val DEFAULT = FindIdUiState(
            email = "",
            errorMessageResId = R.string.empty,
            isVisibleSuccessDialog = false,
            isVisibleNetworkErrorDialog = false,
            errorDialogMessage = null,
        )
    }
}

sealed interface FindIdUiAction : UiAction {
    data class OnChangeEmail(val email: String) : FindIdUiAction
    data object SendEmailToFindId : FindIdUiAction
    data object ConfirmSuccessDialog : FindIdUiAction
    data object ConfirmNetworkErrorDialog : FindIdUiAction
    data object ConfirmErrorDialog : FindIdUiAction
}

sealed interface FindIdEffect : UiEffect {
    data object Complete : FindIdEffect

}

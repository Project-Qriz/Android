package com.qriz.app.feature.mypage

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class MyPageUiState(
    val isLoading: Boolean,
) : UiState {

    companion object {
        val Default = MyPageUiState(
            isLoading = false
        )
    }
}

sealed interface MyPageUiAction : UiAction {
}

sealed interface MyPageUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : MyPageUiEffect
}

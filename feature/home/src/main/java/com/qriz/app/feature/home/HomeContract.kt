package com.qriz.app.feature.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class HomeUiState(
    val isLoading: Boolean,
) : UiState {

    companion object {
        val Default = HomeUiState(
            isLoading = false
        )
    }
}

sealed interface HomeUiAction : UiAction {
}

sealed interface HomeUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : HomeUiEffect
}

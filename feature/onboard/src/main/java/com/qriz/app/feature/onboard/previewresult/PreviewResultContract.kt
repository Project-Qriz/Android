package com.qriz.app.feature.onboard.previewresult

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.onboard.previewresult.model.PreviewTestResultItem

@Immutable
data class PreviewResultUiState(
    val userName: String,
    val previewTestResultItem: PreviewTestResultItem,
    val isLoading: Boolean,
) : UiState {

    companion object {
        val Default = PreviewResultUiState(
            userName = "",
            previewTestResultItem = PreviewTestResultItem.Default,
            isLoading = false
        )
    }
}

sealed interface PreviewResultUiAction : UiAction {
    data object ClickClose : PreviewResultUiAction
    data object LoadPreviewResult : PreviewResultUiAction
    data object ObserveClient : PreviewResultUiAction
}

sealed interface PreviewResultUiEffect : UiEffect {
    data class MoveToWelcomeGuide(val userName: String) : PreviewResultUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : PreviewResultUiEffect
}

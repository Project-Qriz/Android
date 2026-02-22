package com.qriz.app.feature.clip.detail

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState
import com.qriz.app.feature.clip.model.ClipDetailUiModel

@Stable
sealed interface ClipDetailUiState: UiState {
    data object Loading: ClipDetailUiState

    data class Success(val clipDetail: ClipDetailUiModel): ClipDetailUiState

    data class Failure(val message: String): ClipDetailUiState
}

sealed interface ClipDetailUiAction : UiAction {
    data object ClickBack : ClipDetailUiAction
    data class ClickConceptCard(val skillId: Long) : ClipDetailUiAction
    data object ClickGoToStudy : ClipDetailUiAction
}

sealed interface ClipDetailUiEffect : UiEffect {
    data object MoveBack : ClipDetailUiEffect
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null,
    ) : ClipDetailUiEffect
    data class MoveToConceptBookDetail(val skillId: Long) : ClipDetailUiEffect
    data object MoveToConceptBook : ClipDetailUiEffect
}

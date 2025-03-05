package com.qriz.app.feature.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Immutable
data class HomeUiState(
    val isLoading: Boolean,
    val isNeedPreviewTest : Boolean,
    val todayStudyConcepts :List<Int>,
    val currentTodayStudyDay: Int
) : UiState {

    companion object {
        val Default = HomeUiState(
            isLoading = false,
            isNeedPreviewTest = false,
            todayStudyConcepts = List(30) { it + 1 },
            currentTodayStudyDay = 1
        )
    }
}

sealed interface HomeUiAction : UiAction {
    data class ChangeTodayStudyCard(val day: Int) : HomeUiAction
}

sealed interface HomeUiEffect : UiEffect {
    data class ShowSnackBar(
        @StringRes val defaultResId: Int,
        val message: String? = null
    ) : HomeUiEffect
}

package com.qriz.app.feature.base.extention

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.base.UiAction
import com.qriz.app.feature.base.UiEffect
import com.qriz.app.feature.base.UiState

@Composable
inline fun <S : UiState, E : UiEffect, A : UiAction> BaseViewModel<S, E, A>.collectSideEffect(
    crossinline handleSideEffect: (sideEffect: E) -> Unit
) {
    LaunchedEffect(Unit) {
        effect.collect { handleSideEffect(it) }
    }
}

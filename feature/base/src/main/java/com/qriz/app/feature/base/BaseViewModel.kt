package com.qriz.app.feature.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, E : UiEffect, A : UiAction>(
    initialState: S
) : ViewModel() {

    private val _uiState = MutableStateFlow<S>(initialState)
    val uiState = _uiState.asStateFlow()

    private val _effect: Channel<E> = Channel()
    val effect = _effect.receiveAsFlow()

    protected fun updateState(reducer: S.() -> S) {
        val newState = _uiState.value.reducer()
        _uiState.update { newState }
    }

    protected fun sendEffect(effect: E) = viewModelScope.launch {
        _effect.send(effect)
    }

    abstract fun process(action: A): Job
}

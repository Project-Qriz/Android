package com.qriz.app.feature.sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.repository.UserRepository
import com.qriz.app.feature.sign.model.SignInUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<SignInUiState> = MutableStateFlow(
        SignInUiState(
            id = "",
            password = "",
            showPassword = false,
            idErrorMessage = "",
            passwordErrorMessage = ""
        )
    )
    val state: StateFlow<SignInUiState> = _state.asStateFlow()

    fun updateId(id: String) {
        _state.update { it.copy(id = id) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun updateShowPassword(showPassword: Boolean) {
        _state.update { it.copy(showPassword = showPassword) }
    }

    fun login() {
        viewModelScope.launch {
            runCatching {
                userRepository.login(
                    state.value.id,
                    state.value.password,
                )
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}

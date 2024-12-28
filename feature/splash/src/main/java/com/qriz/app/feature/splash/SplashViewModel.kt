package com.qriz.app.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.splash.model.SplashEffect
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    @OptIn(FlowPreview::class)
    val effect: SharedFlow<SplashEffect> = userRepository.flowLogin
        .debounce(2000)
        .map {
            SplashEffect.CheckLogin(it)
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        )
}

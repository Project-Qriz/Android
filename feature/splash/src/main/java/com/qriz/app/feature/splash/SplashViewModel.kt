package com.qriz.app.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qriz.app.feature.splash.model.SplashEffect
import com.qriz.core.data.token.token_api.TokenRepository
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
    tokenRepository: TokenRepository,
) : ViewModel() {

    @OptIn(FlowPreview::class)
    val effect: SharedFlow<SplashEffect> = tokenRepository.flowTokenExist
        .debounce(2000)
        .map { isTokenExist ->
            SplashEffect.CheckLogin(isTokenExist)
        }.shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        )
}

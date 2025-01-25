package com.qriz.app.feature.home

import com.qriz.app.feature.base.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class HomeViewModel @Inject constructor(
) : BaseViewModel<HomeUiState, HomeUiEffect, HomeUiAction>(HomeUiState.Default) {
    override fun process(action: HomeUiAction): Job {
        TODO("Not yet implemented")
    }

}

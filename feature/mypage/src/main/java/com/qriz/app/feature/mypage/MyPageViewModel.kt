package com.qriz.app.feature.mypage

import com.qriz.app.feature.base.BaseViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

class MyPageViewModel @Inject constructor(
) : BaseViewModel<MyPageUiState, MyPageUiEffect, MyPageUiAction>(
    MyPageUiState.Default
) {
    override fun process(action: MyPageUiAction): Job {
        TODO("Not yet implemented")
    }

}

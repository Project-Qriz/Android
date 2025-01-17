package com.qriz.app.feature.onboard.guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qriz.app.feature.onboard.R

@Composable
fun WelcomeGuideScreen(
    userName: String,
    onNext: () -> Unit,
) {
    //TODO : 2.5초 뒤, 자동으로 다음화면 이동
    // 최초 1회만 노출
    GuideBaseScreen(
        title = stringResource(R.string.welcome_guide_title, userName),
        subTitle = stringResource(R.string.welcome_guide_sub_title),
        image = R.drawable.img_onboard_welcome,
        onNext = onNext,
    )
}

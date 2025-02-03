package com.qriz.app.feature.onboard.guide

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qriz.app.feature.onboard.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun WelcomeGuideScreen(
    userName: String,
    moveToHome: () -> Unit,
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isInitialized.value.not()) {
            isInitialized.value = true
            delay(2.5.seconds)
            moveToHome()
        }
    }

    GuideBaseScreen(
        title = stringResource(R.string.welcome_guide_title, userName),
        subTitle = stringResource(R.string.welcome_guide_sub_title),
        image = R.drawable.img_onboard_welcome,
        onNext = moveToHome,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomeGuideScreen() {
    WelcomeGuideScreen(
        userName = "Qriz",
        moveToHome = {}
    )
}

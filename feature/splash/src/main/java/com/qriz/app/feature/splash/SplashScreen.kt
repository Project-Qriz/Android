package com.qriz.app.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.core.designsystem.R as dsR

@Composable
fun SplashScreen(
    moveToMain: (MainTabRoute) -> Unit,
    moveToSurvey: () -> Unit,
    moveToLogin: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    viewModel.collectSideEffect {
        when (it) {
            is SplashUiEffect.MoveToMain -> moveToMain(it.startDestination)
            is SplashUiEffect.MoveToSurvey -> moveToSurvey()
            is SplashUiEffect.MoveToLogin -> moveToLogin()
        }
    }

    SplashContent(
        onInitSplash = { viewModel.process(SplashUiAction.StartLogin) }
    )
}

@Composable
fun SplashContent(
    onInitSplash: () -> Unit
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isInitialized.value.not()) {
            onInitSplash()
            isInitialized.value = true
        }
    }

    Box(
        modifier = Modifier
            .background(color = Blue600)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 225.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(88.dp),
                contentDescription = null,
                painter = painterResource(dsR.drawable.qriz_app_logo)
            )
            Text(
                modifier = Modifier
                    .padding(top = 12.dp),
                text = stringResource(id = R.string.splash_title),
                color = White,
                style = QrizTheme.typography.splash,
            )
        }

        Image(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(233.dp)
                .fillMaxWidth(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            painter = painterResource(R.drawable.splash_background_pattern)
        )

        Image(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 55.dp),
            contentDescription = null,
            painter = painterResource(dsR.drawable.qriz_text_logo_white)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSplashScreen() {
    QrizTheme {
        SplashContent(
            onInitSplash = {}
        )
    }
}

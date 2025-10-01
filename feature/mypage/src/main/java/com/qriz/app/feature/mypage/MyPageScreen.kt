package com.qriz.app.feature.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mypage.component.CustomerServiceSection
import com.qriz.app.feature.mypage.component.FeatureSection
import com.qriz.app.feature.mypage.component.UserSection


@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(), onShowSnackBar: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is MyPageUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    MyPageContent()
}

@Composable
fun MyPageContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Blue50,
            )
    ) {
        QrizTopBar(
            navigationType = NavigationType.NONE,
            title = "마이페이지",
            onNavigationClick = {},
        )

        UserSection(
            modifier = Modifier.padding(
                vertical = 24.dp,
                horizontal = 18.dp,
            ),
            userName = "홍길동",
            onClick = {},
        )

        FeatureSection(
            modifier = Modifier.padding(horizontal = 18.dp),
            onClickReset = {},
            onClickRegisterExam = {},
        )

        CustomerServiceSection(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 32.dp,
            ),
            onClickVersion = {},
            onClickServiceTerms = {},
            onClickPrivacyPolicy = {},
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MyPageContentPreview() {
    QrizTheme {
        MyPageContent()
    }
}

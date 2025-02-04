package com.qriz.app.feature.onboard.previewresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.R

@Composable
fun PreviewResultScreen(
    viewModel: PreviewResultViewModel = hiltViewModel(),
    moveToWelcomeGuide: (String) -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is PreviewResultUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )

            is PreviewResultUiEffect.MoveToWelcomeGuide -> moveToWelcomeGuide(it.userName)
        }
    }

    PreviewResultContent(
        userName = uiState.userName,
        onClickClose = { viewModel.process(PreviewResultUiAction.ClickClose) },
    )
}

@Composable
private fun PreviewResultContent(
    userName: String,
    onClickClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
    ) {
        QrizTopBar(
            navigationType = NavigationType.CLOSE,
            onNavigationClick = onClickClose,
            background = White,
            title = stringResource(R.string.test_result)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .background(Blue50),
        ) {
            //프리뷰 테스트 결과 도넛 그래프 아이템
            //  PreviewTestResultDonutChart
            //틀린 문제에 자주 등장하는 개념 아이템
            //  PreviewFrequentMistakeConceptItem
            //보충하면 좋은 개념 Top2
            //  RecommendedConceptsTop2
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewResultContentPreview() {
    QrizTheme {
        PreviewResultContent(
            userName = "123",
            onClickClose = {},
        )
    }
}

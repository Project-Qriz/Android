package com.qriz.app.feature.onboard.previewresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.previewresult.component.PreviewResultDonutChartCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
        totalScore = uiState.previewTestResultItem.totalScore,
        testResultItems = uiState.previewTestResultItem.testResultItems,
        onInit = { viewModel.process(PreviewResultUiAction.LoadPreviewResult) },
        onClickClose = { viewModel.process(PreviewResultUiAction.ClickClose) },
    )
}

@Composable
private fun PreviewResultContent(
    userName: String,
    totalScore: Int,
    testResultItems: ImmutableList<TestResultItem>,
    onInit: () -> Unit,
    onClickClose: () -> Unit
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isInitialized.value.not()) {
            onInit()
            isInitialized.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                .background(Blue50)
                .padding(horizontal = 18.dp),
        ) {
            PreviewResultDonutChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                userName = userName,
                totalScore = totalScore,
                testResultItems = testResultItems
            )

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
            userName = "Qriz",
            totalScore = 100,
            testResultItems = persistentListOf(
                TestResultItem(
                    scoreName = "1과목",
                    score = 50,
                    color = Blue500
                ),
                TestResultItem(
                    scoreName = "2과목",
                    score = 50,
                    Blue400
                )
            ),
            onInit = {},
            onClickClose = {},
        )
    }
}

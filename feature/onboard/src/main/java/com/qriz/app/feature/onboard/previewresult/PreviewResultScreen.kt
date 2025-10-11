package com.qriz.app.feature.onboard.previewresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.previewresult.component.FrequentMistakeConceptCard
import com.qriz.app.feature.onboard.previewresult.component.PreviewResultDonutChartCard
import com.qriz.app.feature.onboard.previewresult.component.RecommendedConceptsTop2
import com.qriz.app.feature.onboard.previewresult.model.PreviewTestResultItem
import com.qriz.app.feature.onboard.previewresult.model.Ranking
import com.qriz.app.feature.onboard.previewresult.model.WeakAreaItem
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
        previewTestResultItem = uiState.previewTestResultItem,
        state = uiState.state,
        onInit = {
            viewModel.process(PreviewResultUiAction.ObserveClient)
            viewModel.process(PreviewResultUiAction.LoadPreviewResult)
        },
        onClickClose = { viewModel.process(PreviewResultUiAction.ClickClose) },
        onClickRetry = { viewModel.process(PreviewResultUiAction.LoadPreviewResult) },
    )
}

@Composable
private fun PreviewResultContent(
    userName: String,
    previewTestResultItem: PreviewTestResultItem,
    state: PreviewResultUiState.State,
    onInit: () -> Unit,
    onClickClose: () -> Unit,
    onClickRetry: () -> Unit
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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

        when (state) {
            PreviewResultUiState.State.LOADING -> {
                QrizLoading()
            }

            PreviewResultUiState.State.FAILURE -> {
                ErrorScreen(
                    title = stringResource(R.string.fail_get_preview_result),
                    description = stringResource(R.string.please_try_again),
                    onClickRetry = onClickRetry
                )
            }

            PreviewResultUiState.State.SUCCESS -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Blue50)
                        .verticalScroll(scrollState),
                ) {
                    PreviewResultDonutChartCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        userName = userName,
                        totalScore = previewTestResultItem.totalScore,
                        estimatedScore = previewTestResultItem.estimatedScore,
                        testResultItems = previewTestResultItem.testResultItems
                    )

                    if (previewTestResultItem.weakAreas.isNotEmpty()) {
                        FrequentMistakeConceptCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            userName = userName,
                            totalQuestionsCount = previewTestResultItem.totalQuestionsCount,
                            frequentMistakeConcepts = previewTestResultItem.weakAreas,
                            isExistOthersRanking = previewTestResultItem.isExistOthersRanking
                        )
                    }
                    RecommendedConceptsTop2(
                        modifier = Modifier.fillMaxWidth(),
                        topConceptsToImprove = previewTestResultItem.topConceptsToImprove
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewResultContentPreview() {
    QrizTheme {
        PreviewResultContent(
            userName = "Qriz",
            state = PreviewResultUiState.State.SUCCESS,
            previewTestResultItem = PreviewTestResultItem(
                totalScore = 100,
                estimatedScore = 60.0F,
                testResultItems = persistentListOf(
                    TestResultItem(
                        scoreName = "1과목",
                        score = 50,
                    ),
                    TestResultItem(
                        scoreName = "2과목",
                        score = 50,
                    )
                ),
                weakAreas = persistentListOf(
                    WeakAreaItem(
                        ranking = Ranking.FIRST,
                        topic = SQLDConcept.GROUP_BY_AND_HAVING,
                        incorrectCount = 5
                    ),
                    WeakAreaItem(
                        ranking = Ranking.SECOND,
                        topic = SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
                        incorrectCount = 3
                    ),
                    WeakAreaItem(
                        ranking = Ranking.THIRD,
                        topic = SQLDConcept.UNDERSTANDING_TRANSACTIONS,
                        incorrectCount = 1
                    )
                ),
                topConceptsToImprove = persistentListOf(
                    SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
                    SQLDConcept.UNDERSTANDING_TRANSACTIONS,
                ),
                totalQuestionsCount = 20,
            ),
            onInit = {},
            onClickClose = {},
            onClickRetry = {},
        )
    }
}

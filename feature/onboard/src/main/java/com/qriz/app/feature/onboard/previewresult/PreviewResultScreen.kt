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
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.model.TestResultItem
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.previewresult.component.FrequentMistakeConceptCard
import com.qriz.app.feature.onboard.previewresult.component.PreviewResultDonutChartCard
import com.qriz.app.feature.onboard.previewresult.component.RecommendedConceptsTop2
import com.qriz.app.feature.onboard.previewresult.model.Ranking
import com.qriz.app.feature.onboard.previewresult.model.WeakAreaItem
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
        estimatedScore = uiState.previewTestResultItem.estimatedScore,
        testResultItems = uiState.previewTestResultItem.testResultItems,
        questionsCount = uiState.previewTestResultItem.totalQuestions,
        frequentMistakeConcepts = uiState.previewTestResultItem.weakAreas,
        topConceptsToImprove = uiState.previewTestResultItem.topConceptsToImprove,
        onInit = { viewModel.process(PreviewResultUiAction.LoadPreviewResult) },
        onClickClose = { viewModel.process(PreviewResultUiAction.ClickClose) },
    )
}

@Composable
private fun PreviewResultContent(
    userName: String,
    totalScore: Int,
    estimatedScore :Float,
    testResultItems: ImmutableList<TestResultItem>,
    questionsCount: Int,
    frequentMistakeConcepts: ImmutableList<WeakAreaItem>,
    topConceptsToImprove: ImmutableList<SQLDConcept>,
    onInit: () -> Unit,
    onClickClose: () -> Unit
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
        Column(
            modifier = Modifier
                .weight(1f)
                .background(Blue50)
                .padding(horizontal = 18.dp)
                .verticalScroll(scrollState),
        ) {
            PreviewResultDonutChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                userName = userName,
                totalScore = totalScore,
                estimatedScore = estimatedScore,
                testResultItems = testResultItems
            )

            if (frequentMistakeConcepts.isNotEmpty()) {
                FrequentMistakeConceptCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    userName = userName,
                    questionsCount = questionsCount,
                    frequentMistakeConcepts = frequentMistakeConcepts,
                )
            }
            RecommendedConceptsTop2(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                topConceptsToImprove = topConceptsToImprove
            )
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
            questionsCount = 20,
            frequentMistakeConcepts = persistentListOf(
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
            onInit = {},
            onClickClose = {},
            topConceptsToImprove = persistentListOf(
                SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
                SQLDConcept.UNDERSTANDING_TRANSACTIONS,
            )
        )
    }
}

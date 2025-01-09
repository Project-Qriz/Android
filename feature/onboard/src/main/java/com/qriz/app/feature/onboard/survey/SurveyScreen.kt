package com.qriz.app.feature.onboard.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.survey.component.SurveyItemCard
import com.qriz.app.feature.onboard.survey.model.SurveyListItem
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.KnowsAll
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.KnowsNothing
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.SurveyItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ConceptCheckScreen(
    moveToBack: () -> Unit,
    moveToGuide: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: SurveyViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is SurveyUiEffect.MoveToGuide -> moveToGuide()
            SurveyUiEffect.MoveToBack -> moveToBack()
            is SurveyUiEffect.ShowSnackBer -> onShowSnackBar(it.message)
        }
    }

    ConceptCheckContent(
        surveyItems = state.surveyItems,
        isPossibleSubmit = state.isPossibleSubmit,
        onClickKnowsAll = { isChecked ->
            viewModel.process(SurveyUiAction.ClickKnowsAll(isChecked))
        },
        onClickKnowsNothing = { isChecked ->
            viewModel.process(SurveyUiAction.ClickKnowsNothing(isChecked))
        },
        onClickConcept = { preCheckConcept, isChecked ->
            viewModel.process(
                SurveyUiAction.ClickConcept(
                    preCheckConcept = preCheckConcept,
                    isChecked = isChecked
                )
            )
        },
        onClickSubmit = { viewModel.process(SurveyUiAction.ClickSubmit) },
        onClickCancel = { viewModel.process(SurveyUiAction.ClickCancel) },
    )
}

@Composable
private fun ConceptCheckContent(
    surveyItems: ImmutableList<SurveyListItem>,
    isPossibleSubmit: Boolean,
    onClickKnowsAll: (isChecked: Boolean) -> Unit,
    onClickKnowsNothing: (isChecked: Boolean) -> Unit,
    onClickConcept: (preCheckConcept: PreCheckConcept, isChecked: Boolean) -> Unit,
    onClickSubmit: () -> Unit,
    onClickCancel: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        QrizTopBar(
            navigationType = NavigationType.CANCEL,
            onNavigationClick = onClickCancel,
            background = MaterialTheme.colorScheme.background,
        )

        Text(
            text = """아는 개념을 체크해주세요!""",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W600
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 8.dp,
                start = 24.dp,
            ),
        )

        Text(
            text = """체크하신 결과를 토대로
                |추후 진행할 테스트의 레벨이 조정됩니다!""".trimMargin(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(
                start = 24.dp,
                bottom = 32.dp,
            ),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                bottom = 18.dp
            )
        ) {
            //TODO : 수정된 디자인 반영되어야함
            items(
                items = surveyItems,
                key = { surveyItem -> surveyItem.getCategoryId() }
            ) { surveyItem ->
                when (surveyItem) {
                    is KnowsNothing -> {
                        SurveyItemCard(
                            surveyItem = surveyItem,
                            onChecked = { isChecked -> onClickKnowsNothing(isChecked) }
                        )
                    }

                    is KnowsAll -> {
                        SurveyItemCard(
                            surveyItem = surveyItem,
                            onChecked = { isChecked -> onClickKnowsAll(isChecked) }
                        )
                    }

                    is SurveyItem -> {
                        SurveyItemCard(
                            surveyItem = surveyItem,
                            onChecked = { isChecked ->
                                onClickConcept(surveyItem.concept, isChecked)
                            }
                        )
                    }
                }

            }
        }

        QrizButton(
            enable = isPossibleSubmit,
            text = "선택완료",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 18.dp),
            onClick = onClickSubmit,
        )
    }
}

@Preview(
    backgroundColor = 0xFFF8F9FD,
    showBackground = true
)
@Composable
fun ConceptCheckContentPreview() {
    QrizTheme {
        ConceptCheckContent(
            surveyItems = persistentListOf(),
            isPossibleSubmit = false,
            onClickKnowsAll = {},
            onClickKnowsNothing = {},
            onClickConcept = { _, _ -> },
            onClickSubmit = {},
            onClickCancel = {},
        )
    }
}

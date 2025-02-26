package com.qriz.app.feature.onboard.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
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
    moveToPreviewGuide: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    viewModel: SurveyViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is SurveyUiEffect.MoveToGuide -> moveToPreviewGuide()
            is SurveyUiEffect.ShowSnackBar -> onShowSnackBar(it.message)
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
        onClickConcept = { sqldConcept, isChecked ->
            viewModel.process(
                SurveyUiAction.ClickConcept(
                    sqldConcept = sqldConcept,
                    isChecked = isChecked
                )
            )
        },
        onClickSubmit = { viewModel.process(SurveyUiAction.ClickSubmit) },
    )
}

@Composable
private fun ConceptCheckContent(
    surveyItems: ImmutableList<SurveyListItem>,
    isPossibleSubmit: Boolean,
    onClickKnowsAll: (isChecked: Boolean) -> Unit,
    onClickKnowsNothing: (isChecked: Boolean) -> Unit,
    onClickConcept: (sqldConcept: SQLDConcept, isChecked: Boolean) -> Unit,
    onClickSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Blue50)
            .fillMaxSize(),
    ) {

        //TODO : String res 처리 필요
        Text(
            text = """아는 개념을 체크해주세요!""",
            style = QrizTheme.typography.heading1,
            color = Gray800,
            modifier = Modifier.padding(
                top = 48.dp,
                bottom = 8.dp,
                start = 24.dp,
            ),
        )

        Text(
            text = """체크하신 결과를 토대로
                |추후 진행할 테스트의 레벨이 조정됩니다!""".trimMargin(),
            style = QrizTheme.typography.body1Long,
            color = Gray500,
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
private fun ConceptCheckContentPreview() {
    QrizTheme {
        ConceptCheckContent(
            surveyItems = persistentListOf(),
            isPossibleSubmit = false,
            onClickKnowsAll = {},
            onClickKnowsNothing = {},
            onClickConcept = { _, _ -> },
            onClickSubmit = {},
        )
    }
}

package com.qriz.app.feature.onboard.survey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.const.ErrorDialog
import com.qriz.app.core.ui.common.const.NetworkErrorDialog
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.survey.component.SurveyItemCard
import com.qriz.app.feature.onboard.survey.model.SurveyListItem
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.KnowsAll
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.KnowsNothing
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.SurveyItem
import com.qriz.app.feature.onboard.survey.model.SurveyListItem.SurveyItemGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

//TODO: 각 아이템 UI 구조 변경 필요
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
        isExpandSurveyItemGroup = state.isExpandSurveyItemGroup,
        showNetworkErrorDialog = state.showNetworkErrorDialog,
        showErrorDialog = state.showErrorDialog,
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
        onChangeExpandSurveyItemGroup = {
            viewModel.process(SurveyUiAction.ChangeExpandSurveyItemGroup(it))
        },
        onConfirmNetworkErrorDialog = {
            viewModel.process(SurveyUiAction.ConfirmNetworkErrorDialog)
        },
        onConfirmErrorDialog = {
            viewModel.process(SurveyUiAction.ConfirmErrorDialog)
        }
    )
}

@Composable
private fun ConceptCheckContent(
    surveyItems: ImmutableList<SurveyListItem>,
    isPossibleSubmit: Boolean,
    isExpandSurveyItemGroup: Boolean,
    showNetworkErrorDialog: Boolean,
    showErrorDialog: Boolean,
    onClickKnowsAll: (isChecked: Boolean) -> Unit,
    onClickKnowsNothing: (isChecked: Boolean) -> Unit,
    onClickConcept: (sqldConcept: SQLDConcept, isChecked: Boolean) -> Unit,
    onClickSubmit: () -> Unit,
    onChangeExpandSurveyItemGroup: (Boolean) -> Unit,
    onConfirmNetworkErrorDialog: () -> Unit,
    onConfirmErrorDialog: () -> Unit,
) {
    if (showNetworkErrorDialog) {
        NetworkErrorDialog(onConfirmClick = onConfirmNetworkErrorDialog)
    }

    if (showErrorDialog) {
        ErrorDialog(
            description = stringResource(R.string.submit_fail),
            onConfirmClick = onConfirmErrorDialog
        )
    }

    Column(
        modifier = Modifier
            .background(Blue50)
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.check_known_concepts_prompt),
            style = QrizTheme.typography.heading1,
            color = Gray800,
            modifier = Modifier.padding(
                top = 48.dp,
                bottom = 8.dp,
                start = 24.dp,
            ),
        )

        Text(
            text = stringResource(R.string.test_level_adjusted_notice),
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
            for (item in surveyItems) {
                when (item) {
                    is KnowsAll -> item(key = item.getCategoryId()) {
                        SurveyItemCard(
                            text = stringResource(R.string.know_all_about_concepts),
                            isChecked = item.isChecked,
                            onChecked = { isChecked -> onClickKnowsAll(isChecked) },
                            actionItems = {
                                IconButton(
                                    onClick = {
                                        onChangeExpandSurveyItemGroup(isExpandSurveyItemGroup.not())
                                    }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            if (isExpandSurveyItemGroup) R.drawable.ic_keyboard_arrow_up
                                            else R.drawable.ic_keyboard_arrow_down
                                        ),
                                        contentDescription = null,
                                    )
                                }
                            }
                        )
                    }

                    is KnowsNothing -> item(key = item.getCategoryId()) {
                        SurveyItemCard(
                            text = stringResource(R.string.know_nothing_about_concepts),
                            isChecked = item.isChecked,
                            onChecked = { isChecked -> onClickKnowsNothing(isChecked) },
                        )

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Blue100,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    is SurveyItemGroup -> {
                        surveyItems(
                            key = item.getCategoryId(),
                            data = item.list,
                            isExpand = isExpandSurveyItemGroup,
                            onClickConcept = onClickConcept,
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

private fun LazyListScope.surveyItems(
    key: Any?,
    data: ImmutableList<SurveyItem>,
    isExpand: Boolean,
    onClickConcept: (sqldConcept: SQLDConcept, isChecked: Boolean) -> Unit,
) {
    item(key = key) {
        AnimatedVisibility(
            visible = isExpand,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(
                modifier = Modifier.padding(start = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (item in data) {
                    SurveyItemContent(
                        item = item,
                        onClickConcept = onClickConcept
                    )
                }
            }
        }
    }
}

@Composable
private fun SurveyItemContent(
    item: SurveyItem,
    onClickConcept: (sqldConcept: SQLDConcept, isChecked: Boolean) -> Unit,
) {
    SurveyItemCard(
        text = item.concept.title,
        isChecked = item.isChecked,
        onChecked = { isChecked ->
            onClickConcept(
                item.concept,
                isChecked
            )
        },
    )
}

@Preview(
    backgroundColor = 0xFFF8F9FD,
    showBackground = true
)
@Composable
private fun ConceptCheckContentPreview() {
    QrizTheme {
        ConceptCheckContent(
            surveyItems = persistentListOf(
                KnowsNothing(false),
                KnowsAll(false),
                SurveyItemGroup(
                    list = persistentListOf(
                        SurveyItem(
                            concept = SQLDConcept.SELECT,
                            isChecked = false
                        ),
                        SurveyItem(
                            concept = SQLDConcept.SELECT,
                            isChecked = false
                        ),
                        SurveyItem(
                            concept = SQLDConcept.SELECT,
                            isChecked = false
                        ),
                        SurveyItem(
                            concept = SQLDConcept.SELECT,
                            isChecked = false
                        ),
                    )
                )
            ),
            isPossibleSubmit = false,
            isExpandSurveyItemGroup = true,
            showNetworkErrorDialog = false,
            showErrorDialog = false,
            onClickKnowsAll = {},
            onClickKnowsNothing = {},
            onClickConcept = { _, _ -> },
            onClickSubmit = {},
            onChangeExpandSurveyItemGroup = {},
            onConfirmNetworkErrorDialog = {},
            onConfirmErrorDialog = {},
        )
    }
}

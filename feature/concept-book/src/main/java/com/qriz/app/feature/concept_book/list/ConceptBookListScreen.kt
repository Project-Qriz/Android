package com.qriz.app.feature.concept_book.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.ConceptBook
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.concept_book.R
import com.qriz.app.feature.concept_book.component.CategoryCard
import com.qriz.app.feature.concept_book.component.CategoryCardStyle
import com.qriz.app.feature.concept_book.component.ConceptBookTopBar
import com.qriz.app.feature.concept_book.list.component.ConceptBookItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ConceptBookListScreen(
    categoryName: String,
    viewModel: ConceptBookListViewModel = hiltViewModel(),
    moveToConceptBookDetail: (Long) -> Unit,
    moveToBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is ConceptBookListUiEffect.NavigateToConceptBook -> moveToConceptBookDetail(it.id)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(ConceptBookListUiAction.Initialize(categoryName))
    }

    ConceptBookListContent(
        subjectNumber = uiState.subjectNumber,
        categoryName = uiState.categoryName,
        conceptBook = uiState.conceptBooks,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        moveToBack = moveToBack,
        onClickConceptBook = { conceptBookId ->
            viewModel.process(ConceptBookListUiAction.ClickConceptBook(conceptBookId))
        },
    )
}

@Composable
private fun ConceptBookListContent(
    subjectNumber: Int,
    categoryName: String,
    conceptBook: ImmutableList<ConceptBook>,
    isLoading: Boolean,
    errorMessage: String,
    moveToBack: () -> Unit,
    onClickConceptBook: (Long) -> Unit,
) {
    if (errorMessage.isNotEmpty()) {
        QrizDialog(
            title = "오류",
            description = errorMessage,
            confirmText = "확인",
            onConfirmClick = moveToBack,
        )
    }

    if (isLoading) {
        QrizLoading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ConceptBookTopBar(
                subTitle = stringResource(
                    id = R.string.subject,
                    subjectNumber
                ),
                title = categoryName,
                onNavigationClick = moveToBack,
            )
            CategoryCard(
                categoryName = categoryName,
                cardStyle = when (subjectNumber) {
                    1 -> CategoryCardStyle.light
                    2 -> CategoryCardStyle.dark
                    else -> throw IllegalArgumentException("존재하지 않는 과목 => $subjectNumber")
                },
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                    )
                    .width(105.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(all = 16.dp)
            ) {
                items(
                    items = conceptBook,
                    key = { it.hashCode() }) { item ->
                    ConceptBookItem(
                        name = item.name,
                        onClick = { onClickConceptBook(item.id) },
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0F4F7
)
@Composable
private fun ConceptBookListContentPreview() {
    ConceptBookListContent(
        subjectNumber = 1,
        categoryName = "데이터 모델링의 이해",
        conceptBook = persistentListOf(
            ConceptBook(
                id = 1,
                name = "데이터 모델의 이해",
                file = "data_modeling.pdf",
                subjectNumber = 1,
                categoryName = "데이터 모델링의 이해",
            ),
            ConceptBook(
                id = 2,
                name = "데이터 모델의 이해",
                file = "data_modeling.pdf",
                subjectNumber = 1,
                categoryName = "데이터 모델링의 이해",
            ),
            ConceptBook(
                id = 3,
                name = "데이터 모델의 이해",
                file = "data_modeling.pdf",
                subjectNumber = 1,
                categoryName = "데이터 모델링의 이해",
            ),
        ),
        isLoading = false,
        errorMessage = "",
        moveToBack = {},
        onClickConceptBook = {},
    )
}

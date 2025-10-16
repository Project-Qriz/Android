package com.qriz.app.feature.concept_book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.ui.common.provider.LocalPadding
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.concept_book.component.CategoryCardStyle
import com.qriz.app.feature.concept_book.component.SubjectItem

@Composable
fun ConceptBookScreen(
    viewModel: ConceptBookViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    moveToConceptBookList: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is ConceptBookUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(ConceptBookUiAction.Initialize)
    }

    ConceptBookContent(
        subjects = uiState.subjects,
        moveToConceptBookList = moveToConceptBookList,
    )
}

@Composable
fun ConceptBookContent(
    subjects: List<Subject>,
    moveToConceptBookList: (String) -> Unit,
) {
    val scaffoldPadding = LocalPadding.current

    Column(
        modifier = Modifier.padding(scaffoldPadding)
    ) {
        QrizTopBar(
            title = stringResource(R.string.concept_book_title),
            navigationType = NavigationType.NONE,
            onNavigationClick = {},
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            itemsIndexed(
                items = subjects,
            ) { index, item ->
                SubjectItem(
                    subject = item,
                    cardStyle = when (index) {
                        0 -> CategoryCardStyle.light
                        1 -> CategoryCardStyle.dark
                        else -> throw IllegalStateException("Invalid index")
                    },
                    rowCount = 3,
                    onClickCategory = moveToConceptBookList,
                )
            }
        }
    }
}

package com.qriz.app.feature.concept_book.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.Extras
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.util.PdfPageDecoder
import com.qriz.app.feature.concept_book.R
import com.qriz.app.feature.concept_book.component.ConceptBookTopBar

@Composable
fun ConceptBookDetailScreen(
    conceptBookId: Long,
    viewModel: ConceptBookDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.process(
            ConceptBookDetailUiAction.Initialize(conceptBookId)
        )
    }

    ConceptBookDetailContent(
        subjectNumber = state.subjectNumber,
        categoryName = state.categoryName,
        conceptBookTitle = state.conceptBookTitle,
        pdfAsset = state.filePath,
        onBack = onBack,
    )
}

@Composable
private fun ConceptBookDetailContent(
    subjectNumber: Int,
    categoryName: String,
    conceptBookTitle: String,
    pdfAsset: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ConceptBookTopBar(
            subTitle = categoryName,
            title = conceptBookTitle,
            onNavigationClick = onBack,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(
                    vertical = 8.dp,
                    horizontal = 18.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(
                    R.string.subject,
                    subjectNumber
                ),
                style = QrizTheme.typography.subhead,
                color = Gray800,
            )

            Text(
                text = categoryName,
                style = QrizTheme.typography.body2,
                color = Gray800,
            )
        }

        val defaults = ImageRequest.Defaults.DEFAULT.copy(
            extras = Extras.Builder().apply {
                set(
                    Extras.Key(PdfPageDecoder.NUMBER_PDF_PAGES),
                    1
                )
                set(
                    Extras.Key(PdfPageDecoder.SPACE_BETWEEN_PAGES),
                    0
                )
            }.build(),
        )

        Box(
            modifier = Modifier.weight(1f).verticalScroll(state = rememberScrollState())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/$pdfAsset").defaults(defaults).build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}


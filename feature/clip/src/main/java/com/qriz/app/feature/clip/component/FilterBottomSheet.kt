package com.qriz.app.feature.clip.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.clip.R
import com.qriz.app.feature.clip.model.ClipFilterCategoryUiModel
import com.qriz.app.feature.clip.model.ClipFilterConceptUiModel
import com.qriz.app.feature.clip.model.ClipFilterSubjectUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterBottomSheet(
    filters: List<ClipFilterSubjectUiModel>,
    initialPage: Int = 0,
    onApply: (List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pagerState = rememberPagerState(initialPage = initialPage) { 2 }
    val coroutineScope = rememberCoroutineScope()

    var pendingSelected by remember {
        mutableStateOf(filters.flatMap { subject -> subject.categories.flatMap { it.concepts } }
            .filter { it.isSelected }.map { it.name }.toSet())
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
        ) {
            SecondaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 18.dp,
                minTabWidth = 50.dp,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            pagerState.currentPage,
                            matchContentSize = false
                        ),
                        color = Gray800
                    )
                }) {
                filters.mapIndexed { index, element ->
                    Tab(
                        selected = initialPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }) {
                        Text(
                            text = "${element.number}과목",
                            style = QrizTheme.typography.headline2,
                            color = if (pagerState.currentPage == index) Gray800 else Gray400,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            ),
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.Top,
            ) { page ->
                val categories = filters.getOrNull(page)?.categories ?: emptyList()
                FilterBottomSheetPage(
                    categories = categories,
                    pendingSelected = pendingSelected,
                    onConceptToggle = { name ->
                        pendingSelected = if (name in pendingSelected) {
                            pendingSelected - name
                        } else {
                            pendingSelected + name
                        }
                    },
                    onCategorySelectToggle = { category ->
                        val enabledNames = category.concepts.filter { it.enabled }.map { it.name }
                        val allSelected =
                            enabledNames.isNotEmpty() && enabledNames.all { it in pendingSelected }
                        pendingSelected = if (allSelected) {
                            pendingSelected - enabledNames.toSet()
                        } else {
                            pendingSelected + enabledNames.toSet()
                        }
                    },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .width(80.dp),
                    text = stringResource(R.string.reset),
                    style = QrizTheme.typography.body1,
                )

                QrizButton(
                    text = stringResource(R.string.apply),
                    textStyle = QrizTheme.typography.body1,
                    onClick = { onApply(pendingSelected.toList()) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun FilterBottomSheetPage(
    categories: List<ClipFilterCategoryUiModel>,
    pendingSelected: Set<String>,
    onConceptToggle: (String) -> Unit,
    onCategorySelectToggle: (ClipFilterCategoryUiModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 18.dp,
                vertical = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        categories.forEach { category ->
            FilterCategorySection(
                category = category,
                pendingSelected = pendingSelected,
                onConceptToggle = onConceptToggle,
                onSelectAllToggle = { onCategorySelectToggle(category) },
            )
        }
    }
}

@Composable
private fun FilterCategorySection(
    category: ClipFilterCategoryUiModel,
    pendingSelected: Set<String>,
    onConceptToggle: (String) -> Unit,
    onSelectAllToggle: () -> Unit,
) {
    val enabledNames = category.concepts.filter { it.enabled }.map { it.name }
    val allSelected = enabledNames.isNotEmpty() && enabledNames.all { it in pendingSelected }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = category.name,
                style = QrizTheme.typography.headline2,
            )
            TextButton(onClick = onSelectAllToggle) {
                Text(
                    text = if (allSelected) {
                        stringResource(R.string.deselect_all)
                    } else {
                        stringResource(R.string.select_all)
                    },
                    style = QrizTheme.typography.body2.copy(color = Gray600),
                )
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            category.concepts.forEach { concept ->
                FilterChip(
                    value = concept.name,
                    isSelected = concept.name in pendingSelected,
                    enabled = concept.enabled,
                    onClick = { onConceptToggle(concept.name) },
                )
            }
        }
    }
}

// region Preview

private val previewSubject1 = ClipFilterSubjectUiModel(
    number = 1,
    categories = persistentListOf(
        ClipFilterCategoryUiModel(
            name = "데이터 정의어(DDL)",
            concepts = persistentListOf(
                ClipFilterConceptUiModel(
                    "CREATE",
                    isSelected = true,
                    enabled = true
                ),
                ClipFilterConceptUiModel(
                    "ALTER",
                    isSelected = false,
                    enabled = true
                ),
                ClipFilterConceptUiModel(
                    "DROP",
                    isSelected = false,
                    enabled = true
                ),
                ClipFilterConceptUiModel(
                    "TRUNCATE",
                    isSelected = false,
                    enabled = false
                ),
            )
        ),
        ClipFilterCategoryUiModel(
            name = "데이터 조작어(DML)",
            concepts = persistentListOf(
                ClipFilterConceptUiModel(
                    "SELECT",
                    isSelected = true,
                    enabled = true
                ),
                ClipFilterConceptUiModel(
                    "INSERT",
                    isSelected = true,
                    enabled = true
                ),
                ClipFilterConceptUiModel(
                    "DELETE",
                    isSelected = false,
                    enabled = false
                ),
            )
        ),
    )
)

private val previewSubject2 = ClipFilterSubjectUiModel(
    number = 2,
    categories = persistentListOf(
        ClipFilterCategoryUiModel(
            name = "절차형 SQL",
            concepts = persistentListOf(
                ClipFilterConceptUiModel(
                    "프로시저",
                    isSelected = true,
                    enabled = true
                ),
                ClipFilterConceptUiModel(
                    "트리거",
                    isSelected = false,
                    enabled = true
                ),
            )
        ),
    )
)

@Preview(showBackground = true)
@Composable
private fun FilterBottomSheetPagePreview() {
    QrizTheme {
        FilterBottomSheetPage(
            categories = previewSubject1.categories,
            pendingSelected = setOf(
                "CREATE",
                "SELECT"
            ),
            onConceptToggle = {},
            onCategorySelectToggle = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterCategorySectionPreview() {
    QrizTheme {
        FilterCategorySection(
            category = previewSubject1.categories.first(),
            pendingSelected = setOf("CREATE"),
            onConceptToggle = {},
            onSelectAllToggle = {},
        )
    }
}

@Preview
@Composable
private fun FilterBottomSheetPreview() {
    QrizTheme {
        FilterBottomSheet(
            filters = listOf(
                previewSubject1,
                previewSubject2
            ),
            onApply = {},
            onDismiss = {},
        )
    }
}

// endregion

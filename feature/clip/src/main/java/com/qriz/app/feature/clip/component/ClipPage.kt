package com.qriz.app.feature.clip.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.clip.R
import com.qriz.app.feature.clip.model.ClipFilterSubjectUiModel
import com.qriz.app.feature.clip.model.ClipUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun ClipPage(
    selected: String,
    info: ImmutableList<String>,
    data: ImmutableList<ClipUiModel>,
    filters: ImmutableList<ClipFilterSubjectUiModel>,
    onlyIncorrect: Boolean,
    showFilterBottomSheet: Boolean,
    filterInitialPage: Int,
    onSelectStage: (String) -> Unit,
    onApplyFilter: (List<String>) -> Unit,
    onOnlyIncorrectChanged: (Boolean) -> Unit,
    onClickFirstSubjectFilter: () -> Unit,
    onClickSecondSubjectFilter: () -> Unit,
    onDismissFilterBottomSheet: () -> Unit,
    onClickCard: (Long) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOnlyIncorrectExpanded by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTop by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0 ||
                    lazyListState.firstVisibleItemScrollOffset > 300
        }
    }

    val subject1HasFilter = filters.getOrNull(0)?.categories
        ?.flatMap { it.concepts }
        ?.any { it.isSelected } == true
    val subject2HasFilter = filters.getOrNull(1)?.categories
        ?.flatMap { it.concepts }
        ?.any { it.isSelected } == true

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.background(color = Blue50),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DayDropDownMenu(
                        selectedDay = selected,
                        days = info,
                        isExpanded = isExpanded,
                        onExpandedChanged = { isExpanded = it },
                        onSelectDay = onSelectStage,
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(IntrinsicSize.Min),
                    ) {
                        ClipOnlyIncorrectDropDownMenu(
                            expand = isOnlyIncorrectExpanded,
                            onlyIncorrect = onlyIncorrect,
                            onClickFilter = { isOnlyIncorrectExpanded = !isOnlyIncorrectExpanded },
                            onFilterSelected = { value ->
                                onOnlyIncorrectChanged(value)
                                isOnlyIncorrectExpanded = false
                            },
                        )


                        VerticalDivider(
                            color = Gray100,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )

                        FilterButton(
                            text = stringResource(R.string.subject_1),
                            contentColor = if (subject1HasFilter) White else Gray500,
                            containerColor = if (subject1HasFilter) Gray700 else White,
                            borderColor = if (subject1HasFilter) Gray700 else Gray200,
                            icon = com.qriz.app.core.designsystem.R.drawable.arrow_down_icon,
                            onClick = onClickFirstSubjectFilter,
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        FilterButton(
                            text = stringResource(R.string.subject_2),
                            contentColor = if (subject2HasFilter) White else Gray500,
                            containerColor = if (subject2HasFilter) Gray700 else White,
                            borderColor = if (subject2HasFilter) Gray700 else Gray200,
                            icon = com.qriz.app.core.designsystem.R.drawable.arrow_down_icon,
                            onClick = onClickSecondSubjectFilter,
                        )
                    }
                }
            }
            items(data.size) { index ->
                val clip = data[index]
                QuestionResultCard(
                    question = clip.question,
                    isCorrect = clip.correction,
                    title = stringResource(R.string.question_number, clip.questionNum),
                    tag = clip.keyConcepts,
                    onClick = { onClickCard(clip.id) },
                )
            }
        }

        AnimatedVisibility(
            visible = showScrollToTop,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 20.dp),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                shape = CircleShape,
                containerColor = Gray700,
                contentColor = White,
                modifier = Modifier.size(44.dp),
            ) {
                Icon(
                    painter = painterResource(com.qriz.app.core.designsystem.R.drawable.ic_keyboard_arrow_up),
                    contentDescription = null,
                )
            }
        }
    }

    if (showFilterBottomSheet) {
        FilterBottomSheet(
            filters = filters,
            initialPage = filterInitialPage,
            onApply = { selectedConcepts ->
                onApplyFilter(selectedConcepts)
            },
            onDismiss = onDismissFilterBottomSheet,
        )
    }
}

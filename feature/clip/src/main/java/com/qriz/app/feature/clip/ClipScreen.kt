package com.qriz.app.feature.clip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.common.provider.LocalPadding
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.clip.component.ClipPage
import com.qriz.app.feature.clip.component.EmptyClips
import com.qriz.app.feature.clip.model.ClipDataUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ClipScreen(
    viewModel: ClipViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    moveToDailyStudy: (Int, Boolean, Boolean) -> Unit,
    moveToMockTestSessions: () -> Unit,
    moveToClipDetail: (Long) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.process(ClipUiAction.Init)
    }

    viewModel.collectSideEffect {
        when (it) {
            is ClipUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )

            is ClipUiEffect.MoveToDailyStudy -> moveToDailyStudy(
                it.dayNumber,
                it.isReview,
                it.isComprehensiveReview
            )

            is ClipUiEffect.MoveToMockTestSessions -> moveToMockTestSessions()

            is ClipUiEffect.MoveToClipDetail -> moveToClipDetail(it.clipId)
        }
    }

    ClipContent(
        uiState = uiState,
        onAction = viewModel::process,
    )
}

@Composable
fun ClipContent(
    uiState: ClipUiState = ClipUiState.Default,
    onAction: (ClipUiAction) -> Unit = {},
) {
    val pagerState = rememberPagerState { 2 }

    LaunchedEffect(uiState.page) {
        pagerState.animateScrollToPage(uiState.page)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            when (page) {
                ClipUiState.DAILY_STUDY -> onAction(ClipUiAction.TabDailyStudy)
                ClipUiState.MOCK_TEST -> onAction(ClipUiAction.TabMockTest)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalPadding.current),
    ) {
        QrizTopBar(
            title = stringResource(R.string.clip_note),
            navigationType = NavigationType.NONE,
            onNavigationClick = {},
        )
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = White,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(pagerState.currentPage)
                        .padding(horizontal = 10.dp),
                    color = Black,
                )
            },
            divider = { /* no divider */ },
        ) {
            Tab(
                selected = pagerState.currentPage == ClipUiState.DAILY_STUDY,
                onClick = { onAction(ClipUiAction.TabDailyStudy) },
                text = {
                    Text(
                        text = stringResource(R.string.daily),
                        style = QrizTheme.typography.headline2,
                    )
                },
                selectedContentColor = Black,
                unselectedContentColor = Gray400,
                modifier = Modifier.height(48.dp),
            )
            Tab(
                selected = pagerState.currentPage == ClipUiState.MOCK_TEST,
                onClick = { onAction(ClipUiAction.TabMockTest) },
                text = {
                    Text(
                        text = stringResource(R.string.mock_test),
                        style = QrizTheme.typography.headline2,
                    )
                },
                selectedContentColor = Black,
                unselectedContentColor = Gray400,
                modifier = Modifier.height(48.dp),
            )
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                ClipUiState.DAILY_STUDY -> ClipPageContainer(
                    selected = uiState.selectedClipDay,
                    info = uiState.clipDays,
                    clipDataUiState = uiState.dailyStudyClipUiState,
                    onlyIncorrect = uiState.dailyStudyClipOnlyIncorrect,
                    showFilterBottomSheet = uiState.dailyStudyShowFilterBottomSheet,
                    filterInitialPage = uiState.dailyStudyFilterInitialPage,
                    onRetry = {},
                    onSelectInfo = { onAction(ClipUiAction.SelectClipDay(it)) },
                    onApplyFilter = { onAction(ClipUiAction.ApplyDailyStudyFilter(it.toImmutableList())) },
                    onOnlyIncorrectChanged = {
                        onAction(
                            ClipUiAction.ToggleOnlyIncorrect(
                                ClipUiState.DAILY_STUDY,
                                it
                            )
                        )
                    },
                    onClickFirstSubjectFilter = { onAction(ClipUiAction.ClickFirstSubjectFilter(ClipUiState.DAILY_STUDY)) },
                    onClickSecondSubjectFilter = { onAction(ClipUiAction.ClickSecondSubjectFilter(ClipUiState.DAILY_STUDY)) },
                    onDismissFilterBottomSheet = { onAction(ClipUiAction.DismissFilterBottomSheet(ClipUiState.DAILY_STUDY)) },
                    onClickMoveToStudy = { onAction(ClipUiAction.MoveToDailyStudy) },
                    onClickCard = { onAction(ClipUiAction.MoveToClipDetail(it)) },
                )

                ClipUiState.MOCK_TEST -> ClipPageContainer(
                    selected = uiState.selectedClipSession,
                    info = uiState.clipSessions,
                    clipDataUiState = uiState.mockTestClipUiState,
                    onlyIncorrect = uiState.dailyMockTestOnlyIncorrect,
                    showFilterBottomSheet = uiState.mockTestShowFilterBottomSheet,
                    filterInitialPage = uiState.mockTestFilterInitialPage,
                    onRetry = {},
                    onSelectInfo = { onAction(ClipUiAction.SelectClipSession(it)) },
                    onApplyFilter = { onAction(ClipUiAction.ApplyMockTestFilter(it.toImmutableList())) },
                    onOnlyIncorrectChanged = {
                        onAction(
                            ClipUiAction.ToggleOnlyIncorrect(
                                ClipUiState.MOCK_TEST,
                                it
                            )
                        )
                    },
                    onClickFirstSubjectFilter = { onAction(ClipUiAction.ClickFirstSubjectFilter(ClipUiState.MOCK_TEST)) },
                    onClickSecondSubjectFilter = { onAction(ClipUiAction.ClickSecondSubjectFilter(ClipUiState.MOCK_TEST)) },
                    onDismissFilterBottomSheet = { onAction(ClipUiAction.DismissFilterBottomSheet(ClipUiState.MOCK_TEST)) },
                    onClickMoveToStudy = { onAction(ClipUiAction.MoveToMockTestSessions) },
                    onClickCard = { onAction(ClipUiAction.MoveToClipDetail(it)) },
                )
            }
        }
    }
}

@Composable
private fun ClipPageContainer(
    selected: String,
    info: ImmutableList<String>,
    clipDataUiState: ClipDataUiState,
    onlyIncorrect: Boolean,
    showFilterBottomSheet: Boolean,
    filterInitialPage: Int,
    onRetry: () -> Unit,
    onSelectInfo: (String) -> Unit,
    onApplyFilter: (List<String>) -> Unit,
    onOnlyIncorrectChanged: (Boolean) -> Unit,
    onClickFirstSubjectFilter: () -> Unit,
    onClickSecondSubjectFilter: () -> Unit,
    onDismissFilterBottomSheet: () -> Unit,
    onClickMoveToStudy: () -> Unit,
    onClickCard: (Long) -> Unit,
) {
    when (clipDataUiState) {
        is ClipDataUiState.Success -> if (clipDataUiState.clips.isNotEmpty()) {
            ClipPage(
                selected = selected,
                info = info,
                data = clipDataUiState.clips,
                filters = clipDataUiState.filter,
                onlyIncorrect = onlyIncorrect,
                showFilterBottomSheet = showFilterBottomSheet,
                filterInitialPage = filterInitialPage,
                onSelectStage = onSelectInfo,
                onApplyFilter = onApplyFilter,
                onOnlyIncorrectChanged = onOnlyIncorrectChanged,
                onClickFirstSubjectFilter = onClickFirstSubjectFilter,
                onClickSecondSubjectFilter = onClickSecondSubjectFilter,
                onDismissFilterBottomSheet = onDismissFilterBottomSheet,
                onClickCard = onClickCard,
            )
        } else {
            EmptyClips(onClickToStudy = onClickMoveToStudy)
        }

        is ClipDataUiState.Loading -> QrizLoading(modifier = Modifier.fillMaxSize())
        is ClipDataUiState.Failure -> ErrorScreen(
            title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
            description = clipDataUiState.message,
            onClickRetry = onRetry,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IncorrectAnswersNoteContentPreview() {
    QrizTheme {
        ClipContent()
    }
}

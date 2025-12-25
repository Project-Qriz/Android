package com.qriz.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.common.const.ExamScheduleBottomSheet
import com.qriz.app.core.ui.common.provider.LocalPadding
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.home.HomeUiState.HomeDataLoadState
import com.qriz.app.feature.home.component.DailyStudyPlanDayFilterBottomSheet
import com.qriz.app.feature.home.component.ExamScheduleCard
import com.qriz.app.feature.home.component.TestStartCard
import com.qriz.app.feature.home.component.TodayStudyCardPager
import com.qriz.app.feature.home.component.UserExamUiState
import com.qriz.app.feature.home.component.WeeklyCustomConcept
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.NOT_STARTED
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.qriz.app.core.designsystem.R as DSR
import com.qriz.app.core.ui.common.R as UR

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    moveToPreviewTest: () -> Unit,
    moveToDailyStudy: (Int, Boolean, Boolean) -> Unit,
    moveToMockTestSessions: () -> Unit,
    moveToConceptBook: (Long) -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is HomeUiEffect.MoveToPreviewTest -> moveToPreviewTest()
            is HomeUiEffect.MoveToDailyStudy -> moveToDailyStudy(
                it.dayNumber,
                it.isReview,
                it.isComprehensiveReview
            )
            is HomeUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )

            is HomeUiEffect.MoveToMockTestSessions -> moveToMockTestSessions()
            is HomeUiEffect.MoveToConceptBook -> moveToConceptBook(it.id)
        }
    }

    if (uiState.applyExamErrorMessage != null) {
        QrizDialog(
            title = stringResource(UR.string.error_occurs),
            description = uiState.applyExamErrorMessage!!,
            confirmText = stringResource(UR.string.retry),
            cancelText = stringResource(R.string.to_home),
            onCancelClick = { viewModel.process(HomeUiAction.DismissApplyExamErrorDialog) },
            onConfirmClick = { },
        )
    }

    if (uiState.examSchedulesErrorMessage != null) {
        QrizDialog(
            title = stringResource(UR.string.error_occurs),
            description = uiState.examSchedulesErrorMessage!!,
            confirmText = stringResource(UR.string.retry),
            cancelText = stringResource(R.string.to_home),
            onCancelClick = { viewModel.process(HomeUiAction.DismissExamSchedulesErrorDialog) },
            onConfirmClick = { viewModel.process(HomeUiAction.LoadToExamSchedules) },
        )
    }

    if (uiState.isShowExamScheduleBottomSheet) {
        ExamScheduleBottomSheet(
            schedulesLoadState = uiState.schedulesState,
            onClickRetry = { viewModel.process(HomeUiAction.LoadToExamSchedules) },
            onSelectExamDate = { viewModel.process(HomeUiAction.ClickExamSchedule(it)) },
            onDismissRequest = { viewModel.process(HomeUiAction.DismissTestDateBottomSheet) },
        )
    }

    if (uiState.showPlanDayFilterBottomSheet) {
        DailyStudyPlanDayFilterBottomSheet(
            selectedDay = uiState.selectedPlanDay,
            onSelectDay = { viewModel.process(HomeUiAction.ChangeStudyPlanDate(it)) },
            onClickToday = { viewModel.process(HomeUiAction.ChangeStudyPlanDateToToday) },
            onDismissRequest = { viewModel.process(HomeUiAction.DismissPlanDayFilterBottomSheet) },
        )
    }

    if (uiState.showResetPlanConfirmationDialog) {
        QrizDialog(
            title = stringResource(R.string.reset_plan),
            description = stringResource(R.string.plan_will_be_reset),
            cancelText = stringResource(DSR.string.cancel),
            onConfirmClick = {
                viewModel.process(HomeUiAction.ResetDailyStudyPlans)
                viewModel.process(HomeUiAction.DismissResetPlanConfirmationDialog)
            },
            onCancelClick = {
                viewModel.process(HomeUiAction.DismissResetPlanConfirmationDialog)
            },
            onDismissRequest = {
                viewModel.process(HomeUiAction.DismissResetPlanConfirmationDialog)
            },
        )
    }

    uiState.resetPlanErrorMessage?.let {
        QrizDialog(
            title = stringResource(R.string.plan_reset_fail),
            description = it,
            onConfirmClick = {
                viewModel.process(HomeUiAction.DismissResetPlanErrorDialog)
            },
            onDismissRequest = {
                viewModel.process(HomeUiAction.DismissResetPlanErrorDialog)
            }
        )
    }

    HomeContent(
        userName = uiState.user.name,
        previewTestStatus = uiState.user.previewTestStatus,
        selectedPlanDay = uiState.selectedPlanDay,
        scheduleState = uiState.userExamState,
        dataLoadState = uiState.dataLoadState,
        dailyStudyPlans = uiState.dailyStudyPlans,
        weeklyRecommendation = uiState.weeklyRecommendation,
        onInit = { viewModel.process(HomeUiAction.ObserveClient) },
        onClickExamApply = { viewModel.process(HomeUiAction.ClickApply) },
        onClickMockTest = { viewModel.process(HomeUiAction.ClickMockTestCard) },
        onClickPreviewTest = { viewModel.process(HomeUiAction.MoveToPreviewTest) },
        onChangeTodayStudyCard = { viewModel.process(HomeUiAction.ChangeStudyPlanDate(it)) },
        onClickWeeklyCustomConcept = { viewModel.process(HomeUiAction.ClickRecommendationCard(it)) },
        onClickPlanDayFilter = { viewModel.process(HomeUiAction.ShowPlanDayFilterBottomSheet) },
        onClickRetryLoadHomeData = {},
        onClickResetDailyStudyPlan = { viewModel.process(HomeUiAction.ShowResetPlanConfirmationDialog) },
        onClickGoToStudy = { viewModel.process(HomeUiAction.ClickMoveToDailyStudy) },
    )
}

@Composable
fun HomeContent(
    userName: String,
    previewTestStatus: PreviewTestStatus,
    selectedPlanDay: Int,
    dataLoadState: HomeDataLoadState,
    scheduleState: UserExamUiState,
    dailyStudyPlans: ImmutableList<DailyStudyPlan>,
    weeklyRecommendation: ImmutableList<WeeklyRecommendation>,
    onInit: () -> Unit,
    onClickExamApply: () -> Unit,
    onClickPlanDayFilter: () -> Unit,
    onClickMockTest: () -> Unit,
    onClickPreviewTest: () -> Unit,
    onChangeTodayStudyCard: (Int) -> Unit,
    onClickWeeklyCustomConcept: (Long) -> Unit,
    onClickRetryLoadHomeData: () -> Unit,
    onClickResetDailyStudyPlan: () -> Unit,
    onClickGoToStudy: () -> Unit,
) {
    val isInitialized = rememberSaveable { mutableStateOf(false) }
    val padding = LocalPadding.current

    LaunchedEffect(Unit) {
        if (isInitialized.value.not()) {
            onInit()
            isInitialized.value = true
        }
    }

    val horizontalPadding = remember { 18.dp }
    Column(
        modifier = Modifier
            .background(Blue50)
            .statusBarsPadding()
            .fillMaxSize()
            .padding(padding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(DSR.drawable.qriz_app_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(end = 8.dp)
                    .width(32.dp)
                    .height(32.dp),
            )
            Image(
                painter = painterResource(DSR.drawable.qriz_text_logo_white),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Black),
                modifier = Modifier
                    .width(62.dp)
                    .height(21.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            when (dataLoadState) {
                is HomeDataLoadState.Loading -> {
                    QrizLoading()
                }

                is HomeDataLoadState.Failure -> {
                    ErrorScreen(
                        title = stringResource(R.string.error_occurred),
                        description = dataLoadState.message,
                        onClickRetry = onClickRetryLoadHomeData,
                    )
                }

                is HomeDataLoadState.Success -> {
                    ExamScheduleCard(
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(
                                top = 24.dp,
                                bottom = 32.dp
                            ),
                        userName = userName,
                        scheduleState = scheduleState,
                        onClickApply = onClickExamApply,
                    )

                    TestStartCard(
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 32.dp),
                        isNeedPreviewTest = previewTestStatus.isNeedPreviewTest(),
                        onClickMockTest = onClickMockTest,
                        onClickPreviewTest = onClickPreviewTest
                    )

                    TodayStudyCardPager(
                        horizontalPadding = horizontalPadding,
                        isNeedPreviewTest = previewTestStatus.isNeedPreviewTest(),
                        selectedPlanDay = selectedPlanDay,
                        dailyStudyPlans = dailyStudyPlans,
                        onChangeTodayStudyCard = onChangeTodayStudyCard,
                        onClickDayFilter = onClickPlanDayFilter,
                        onClickResetDailyStudyPlan = onClickResetDailyStudyPlan,
                        onClickGoToStudy = onClickGoToStudy
                    )

                    WeeklyCustomConcept(
                        isNeedPreviewTest = previewTestStatus.isNeedPreviewTest(),
                        recommendations = weeklyRecommendation,
                        onClick = onClickWeeklyCustomConcept,
                    )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    heightDp = 1500
)
@Composable
fun HomeContentPreview() {
    QrizTheme {
        HomeContent(
            userName = "Qriz",
            previewTestStatus = NOT_STARTED,
            selectedPlanDay = 0,
            dataLoadState = HomeDataLoadState.Success,
            scheduleState = UserExamUiState.NoSchedule,
            dailyStudyPlans = persistentListOf(),
            weeklyRecommendation = persistentListOf(),
            onInit = {},
            onClickExamApply = {},
            onClickPreviewTest = {},
            onClickMockTest = {},
            onChangeTodayStudyCard = {},
            onClickWeeklyCustomConcept = {},
            onClickPlanDayFilter = {},
            onClickRetryLoadHomeData = {},
            onClickResetDailyStudyPlan = {},
            onClickGoToStudy = {},
        )
    }
}

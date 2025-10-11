package com.qriz.app.feature.mypage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.const.ExamScheduleBottomSheet
import com.qriz.app.core.ui.common.const.ExamScheduleState
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mypage.component.CustomerServiceSection
import com.qriz.app.feature.mypage.component.FeatureSection
import com.qriz.app.feature.mypage.component.ResetPlanDialog
import com.qriz.app.feature.mypage.component.UserSection

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    moveToSetting: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is MyPageUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )

            is MyPageUiEffect.NavigateSetting -> moveToSetting()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(MyPageUiAction.LoadData)
    }

    MyPageContent(
        userName = uiState.user.name,
        examScheduleState = uiState.examScheduleState,
        showExamBottomSheet = uiState.showExamBottomSheet,
        showResetPlanDialog = uiState.showResetPlanDialog,
        onClickProfile = { viewModel.process(MyPageUiAction.ClickProfile) },
        onConfirmResetPlan = { viewModel.process(MyPageUiAction.ResetPlan) },
        onClickResetPlan = { viewModel.process(MyPageUiAction.ShowResetPlanDialog) },
        onClickRegisterExamSchedule = { viewModel.process(MyPageUiAction.ShowExamBottomSheet) },
        onSelectExamSchedule = { viewModel.process(MyPageUiAction.ClickExamSchedule(it)) },
        onDismissExamBottomSheet = { viewModel.process(MyPageUiAction.DismissExamBottomSheet) },
        onDismissResetPlanDialog = { viewModel.process(MyPageUiAction.DismissResetPlanDialog) },
        onRetry = { viewModel.process(MyPageUiAction.LoadData) }
    )
}

@Composable
fun MyPageContent(
    userName: String,
    examScheduleState: ExamScheduleState,
    showResetPlanDialog: Boolean,
    showExamBottomSheet: Boolean,
    onClickProfile: () -> Unit,
    onClickRegisterExamSchedule: () -> Unit,
    onSelectExamSchedule: (Long) -> Unit,
    onClickResetPlan: () -> Unit,
    onConfirmResetPlan: () -> Unit,
    onDismissExamBottomSheet: () -> Unit,
    onDismissResetPlanDialog: () -> Unit,
    onRetry: () -> Unit,
) {
    if (showResetPlanDialog) {
        ResetPlanDialog(
            onConfirm = onConfirmResetPlan,
            onDismissRequest = onDismissResetPlanDialog
        )
    }

    if (showExamBottomSheet) {
        ExamScheduleBottomSheet(
            schedulesLoadState = examScheduleState,
            onDismissRequest = onDismissExamBottomSheet,
            onSelectExamDate = onSelectExamSchedule,
            onClickRetry = onRetry,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Blue50,
            )
    ) {
        QrizTopBar(
            navigationType = NavigationType.NONE,
            title = stringResource(R.string.my_title),
            onNavigationClick = {},
        )

        UserSection(
            modifier = Modifier.padding(
                vertical = 24.dp,
                horizontal = 18.dp,
            ),
            userName = userName,
            onClick = onClickProfile,
        )

        FeatureSection(
            modifier = Modifier.padding(horizontal = 18.dp),
            onClickResetPlan = onClickResetPlan,
            onClickRegisterExam = onClickRegisterExamSchedule,
        )

        CustomerServiceSection(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 32.dp,
            ),
            onClickVersion = {},
            onClickServiceTerms = {},
            onClickPrivacyPolicy = {},
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MyPageContentPreview() {
    QrizTheme {
        MyPageContent(
            userName = "홍길동",
            examScheduleState = ExamScheduleState.Loading,
            showResetPlanDialog = false,
            showExamBottomSheet = false,
            onClickProfile = {},
            onClickRegisterExamSchedule = {},
            onSelectExamSchedule = {},
            onClickResetPlan = {},
            onConfirmResetPlan = {},
            onDismissExamBottomSheet = {},
            onDismissResetPlanDialog = {},
            onRetry = {}
        )
    }
}

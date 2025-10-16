package com.qriz.app.feature.daily_study.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.daily_study.daily_study_api.model.SimplePlannedSkill
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.daily_study.status.component.PlannedSkillCard
import com.qriz.app.feature.daily_study.status.component.TestCompleteTooltip
import com.qriz.app.feature.daily_study.status.component.TestStatusCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun DailyStudyPlanStatusScreen(
    viewModel: DailyStudyPlanStatusViewModel = hiltViewModel(),
    moveToResult: (Int) -> Unit,
    moveToTest: (Int) -> Unit,
    moveToBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is DailyStudyPlanStatusUiEffect.MoveToTest -> moveToTest(it.day)
            is DailyStudyPlanStatusUiEffect.MoveToResult -> moveToResult(it.day)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(DailyStudyPlanStatusUiAction.LoadData)
    }

    if (uiState.showRetryConfirmationDialog) {
        QrizDialog(
            title = stringResource(R.string.test_retry),
            description = stringResource(R.string.test_already_taken_warning),
            confirmText = stringResource(DSR.string.confirmation),
            cancelText = stringResource(DSR.string.cancel),
            onConfirmClick = { viewModel.process(DailyStudyPlanStatusUiAction.MoveToTest) },
            onCancelClick = { viewModel.process(DailyStudyPlanStatusUiAction.DismissRetryConfirmDialog) },
        )
    }

    DailyStudyPlanStatusContent(
        isLoading = uiState.isLoading,
        skillsTitleResId = uiState.skillTitleResId,
        testMessageResId = uiState.testMessageResId,
        skills = uiState.skills,
        errorMessage = uiState.errorMessage,
        testScore = uiState.score,
        canRetry = uiState.canRetry,
        testStatusTextResId = uiState.testStatusTextResId,
        testStatusTextColor = uiState.testStatusTextColor,
        testStatusIconColor = uiState.testCardIconColor,
        testStatusBackgroundColor = uiState.testCardBackgroundColor,
        complete = uiState.isComplete,
        onClickRetry = { viewModel.process(DailyStudyPlanStatusUiAction.LoadData) },
        onClickStatusCard = { viewModel.process(DailyStudyPlanStatusUiAction.ClickTestCard) },
        moveToBack = moveToBack,
    )
}

@Composable
private fun DailyStudyPlanStatusContent(
    isLoading: Boolean,
    skillsTitleResId: Int,
    testMessageResId: Int,
    skills: ImmutableList<SimplePlannedSkill>,
    errorMessage: String?,
    testStatusTextResId: Int,
    testStatusTextColor: Color,
    testStatusBackgroundColor: Color,
    testStatusIconColor: Color,
    testScore: Double,
    canRetry: Boolean,
    complete: Boolean,
    onClickStatusCard: () -> Unit,
    onClickRetry: () -> Unit,
    moveToBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Blue50)
            .navigationBarsPadding()
    ) {
        QrizTopBar(
            title = stringResource(R.string.daily_study),
            onNavigationClick = moveToBack,
            navigationType = NavigationType.BACK,
        )

        if (isLoading) {
            QrizLoading(modifier = Modifier.fillMaxSize())
        }

        if (errorMessage != null) {
            ErrorScreen(
                title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
                description = errorMessage,
                onClickRetry = onClickRetry,
            )
        }

        if (errorMessage == null && isLoading.not()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        vertical = 32.dp,
                        horizontal = 18.dp,
                    )
            ) {
                Text(
                    text = stringResource(skillsTitleResId),
                    style = QrizTheme.typography.heading2.copy(color = Gray800),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                for (skill in skills) {
                    PlannedSkillCard(
                        modifier = Modifier.padding(bottom = 8.dp),
                        keyConcept = skill.keyConcept,
                        description = skill.description
                    )
                }

                Text(
                    text = stringResource(R.string.related_test),
                    style = QrizTheme.typography.heading2.copy(color = Gray800),
                    modifier = Modifier.padding(
                        top = 24.dp,
                        bottom = 8.dp
                    )
                )

                Text(
                    text = stringResource(testMessageResId),
                    style = QrizTheme.typography.body2.copy(color = Gray500),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TestStatusCard(
                    score = testScore,
                    canRetry = canRetry,
                    statusTextResId = testStatusTextResId,
                    statusTextColor = testStatusTextColor,
                    iconColor = testStatusIconColor,
                    backgroundColor = testStatusBackgroundColor,
                    onClick = onClickStatusCard,
                )

                if (complete) {
                    TestCompleteTooltip(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.go_to_result)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DailyStudyStudyPlanStatusContentPreview() {
    QrizTheme {
        DailyStudyPlanStatusContent(
            isLoading = false,
            errorMessage = null,
            skillsTitleResId = R.string.daily_study,
            testMessageResId = R.string.completed_plan,
            skills = persistentListOf(
                SimplePlannedSkill(
                    id = 1,
                    keyConcept = "데이터 모델의 이해",
                    description = "JOIN은 두 개 이상의 테이블을 연결하여 데이터를 출력하는 것을 의미한다."
                ),
                SimplePlannedSkill(
                    id = 2,
                    keyConcept = "엔티티",
                    description = "JOIN은 두 개 이상의 테이블을 연결하여 데이터를 출력하는 것을 의미한다."
                ),

                ),
            testScore = 40.0,
            canRetry = false,
            onClickRetry = {},
            testStatusTextResId = R.string.study_complete,
            testStatusTextColor = Blue500,
            testStatusBackgroundColor = White,
            testStatusIconColor = Gray800,
            complete = true,
            onClickStatusCard = {},
            moveToBack = {},
        )
    }
}

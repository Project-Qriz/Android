package com.qriz.app.feature.daily_study.study

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.QrizDialog
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.core.ui.test.TestScreen
import com.qriz.app.core.ui.test.TestTimeType
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.QuestionTestItem
import com.qriz.app.featrue.daily_study.R
import com.qriz.app.feature.base.extention.collectSideEffect
import kotlinx.collections.immutable.ImmutableList
import com.qriz.app.core.ui.common.R as UCR

@Composable
fun DailyTestScreen(
    viewModel: DailyTestViewModel = hiltViewModel(),
    moveToResult: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    moveToBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect {
        when (it) {
            is DailyStudyUiEffect.MoveToResult -> moveToResult()
            is DailyStudyUiEffect.Cancel -> moveToBack()
            is DailyStudyUiEffect.ShowSnackbar -> onShowSnackBar(it.message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(DailyTestUiAction.LoadData)
    }

    if (uiState.showErrorDialog) {
        QrizDialog(
            title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
            description = uiState.errorMessage ?: UNKNOWN_ERROR,
            onConfirmClick = { viewModel.process(DailyTestUiAction.DismissErrorDialog) }
        )
    }

    if (uiState.showCancelConfirmationDialog) {
        QrizDialog(title = stringResource(R.string.cancel_test),
            description = stringResource(R.string.cancel_test_warning),
            confirmText = stringResource(R.string.cancel),
            cancelText = stringResource(R.string.continue_test),
            onConfirmClick = { viewModel.process(DailyTestUiAction.CancelTest) },
            onCancelClick = { viewModel.process(DailyTestUiAction.DismissCancelConfirmationDialog) },
            onDismissRequest = { viewModel.process(DailyTestUiAction.DismissCancelConfirmationDialog) })
    }

    if (uiState.showSubmitConfirmationDialog) {
        QrizDialog(
            title = stringResource(R.string.confirm_submit),
            description = stringResource(R.string.submit_warning),
            cancelText = stringResource(com.qriz.app.core.ui.test.R.string.cancel),
            onConfirmClick = { viewModel.process(DailyTestUiAction.SubmitTest) },
            onCancelClick = { viewModel.process(DailyTestUiAction.DismissSubmitConfirmationDialog) },
            onDismissRequest = { viewModel.process(DailyTestUiAction.DismissSubmitConfirmationDialog) },
        )
    }

    when (val loadState = uiState.state) {
        is DailyTestUiState.LoadState.Success -> StudyContent(
            questions = loadState.questions,
            currentIndex = uiState.currentIndex,
            remainTimeText = uiState.remainTimeText,
            progressPercent = uiState.progressPercent,
            onSelectOption = { questionId, option ->
                viewModel.process(
                    DailyTestUiAction.SelectOption(
                        questionId = questionId,
                        option = option,
                    )
                )
            },
            onClickCancel = { viewModel.process(DailyTestUiAction.ShowCancelConfirmationDialog) },
            onClickSubmit = { viewModel.process(DailyTestUiAction.ShowSubmitConfirmationDialog) },
            onClickNext = { viewModel.process(DailyTestUiAction.NextQuestion) },
        )

        is DailyTestUiState.LoadState.Failure -> ErrorScreen(title = stringResource(UCR.string.error_occurs),
            description = loadState.errorMessage,
            onClickRetry = { viewModel.process(DailyTestUiAction.LoadData) })

        is DailyTestUiState.LoadState.Loading -> QrizLoading()
    }
}

@Composable
private fun StudyContent(
    questions: ImmutableList<QuestionTestItem>,
    currentIndex: Int,
    remainTimeText: String,
    progressPercent: Float,
    onSelectOption: (Long, OptionItem) -> Unit,
    onClickNext: () -> Unit,
    onClickSubmit: () -> Unit,
    onClickCancel: () -> Unit,
) {
    TestScreen(
        questions = questions,
        testTimeType = TestTimeType.INDIVIDUAL,
        currentIndex = currentIndex,
        remainTimeText = remainTimeText,
        progressPercent = progressPercent,
        isLoading = false,
        onSelectOption = { id, option ->
            onSelectOption(
                id,
                option
            )
        },
        onClickNextPage = onClickNext,
        onClickCancel = onClickCancel,
        onClickSubmit = onClickSubmit,
        onClickPreviousPage = onClickCancel,
    )
}

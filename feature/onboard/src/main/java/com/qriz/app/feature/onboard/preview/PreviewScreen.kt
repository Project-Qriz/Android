package com.qriz.app.feature.onboard.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.TestScreen
import com.qriz.app.core.ui.test.TestSubmitWarningDialog
import com.qriz.app.core.ui.test.TestTimeType
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.guide.TestEndWarningDialog
import com.qriz.app.feature.onboard.preview.PreviewUiState.LoadStatus.*

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel(),
    moveToPreviewResult: () -> Unit,
    moveToHome: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is PreviewUiEffect.MoveToResult -> moveToPreviewResult()
            is PreviewUiEffect.MoveToHome -> moveToHome()
            is PreviewUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    if (uiState.isVisibleTestSubmitWarningDialog) {
        TestSubmitWarningDialog(
            onCancelClick = { viewModel.process(PreviewUiAction.ClickDismissTestSubmitWarningDialog) },
            onConfirmClick = { viewModel.process(PreviewUiAction.ClickConfirmTestSubmitWarningDialog) },
        )
    }

    if (uiState.isVisibleTestEndWarningDialog) {
        TestEndWarningDialog(
            onCancelClick = { viewModel.process(PreviewUiAction.ClickDismissTestEndWarningDialog) },
            onConfirmClick = { viewModel.process(PreviewUiAction.ClickConfirmTestEndWarningDialog) },
        )
    }

    when(uiState.loadStatus) {
        Loading -> QrizLoading()
        Success -> {
            TestScreen(
                questions = uiState.questions,
                testTimeType = TestTimeType.TOTAL,
                progressPercent = uiState.progressPercent,
                remainTimeText = uiState.remainTimeText,
                currentIndex = uiState.currentIndex,
                isLoading = false,
                onSelectOption = { id, option ->
                    viewModel.process(PreviewUiAction.SelectOption(questionID = id, option = option))
                },
                onClickNextPage = { viewModel.process(PreviewUiAction.ClickNextPage) },
                onClickSubmit = { viewModel.process(PreviewUiAction.ClickSubmit) },
                onClickPreviousPage = { viewModel.process(PreviewUiAction.ClickPreviousPage) },
                onClickCancel = { viewModel.process(PreviewUiAction.ClickCancel) },
            )
        }
        Failure -> {
            ErrorScreen(
                title = stringResource(R.string.failed_get_test),
                description = stringResource(R.string.please_try_again),
                onClickRetry = {
                    viewModel.process(PreviewUiAction.ObservePreviewTestItem)
                }
            )
        }
    }
}

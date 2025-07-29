package com.qriz.app.feature.mock_test.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.TestEndWarningDialog
import com.qriz.app.core.ui.test.TestScreen
import com.qriz.app.core.ui.test.TestSubmitWarningDialog
import com.qriz.app.core.ui.test.TestTimeType
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.core.ui.common.R as UCR

@Composable
fun MockTestScreen(
    viewModel: MockTestViewModel = hiltViewModel(),
    moveToResult: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is MockTestUiEffect.MoveToResult -> moveToResult()
            is MockTestUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
            is MockTestUiEffect.CancelTest -> onBack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(MockTestUiAction.ObserveMockTestItem)
    }

    if (uiState.isVisibleTestSubmitWarningDialog) {
        TestSubmitWarningDialog(
            onCancelClick = { viewModel.process(MockTestUiAction.ClickDismissTestSubmitWarningDialog) },
            onConfirmClick = { viewModel.process(MockTestUiAction.ClickConfirmTestSubmitWarningDialog) },
        )
    }

    if (uiState.isVisibleTestEndWarningDialog) {
        TestEndWarningDialog(
            onCancelClick = { viewModel.process(MockTestUiAction.ClickConfirmTestEndWarningDialog) },
            onConfirmClick = {
                viewModel.process(MockTestUiAction.ClickDismissTestEndWarningDialog)
            },
        )
    }

    when (val state = uiState.state) {
        is MockTestUiState.LoadState.Failure -> ErrorScreen(
            title = stringResource(UCR.string.error_occurs),
            description = state.errorMessage,
            onClickRetry = {

            },
        )

        is MockTestUiState.LoadState.Loading -> QrizLoading()

        is MockTestUiState.LoadState.Success -> TestScreen(
            questions = state.questions,
            testTimeType = TestTimeType.TOTAL,
            progressPercent = uiState.progressPercent,
            remainTimeText = uiState.remainTimeText,
            currentIndex = uiState.currentIndex,
            isLoading = false,
            onSelectOption = { id, option ->
                viewModel.process(
                    MockTestUiAction.SelectOption(
                        questionID = id,
                        option = option
                    )
                )
            },
            onClickNextPage = { viewModel.process(MockTestUiAction.ClickNextPage) },
            onClickSubmit = { viewModel.process(MockTestUiAction.ClickSubmit) },
            onClickPreviousPage = { viewModel.process(MockTestUiAction.ClickPreviousPage) },
            onClickCancel = { viewModel.process(MockTestUiAction.ClickCancel) },
        )
    }
}

package com.qriz.app.feature.onboard.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.ui.test.TestScreen
import com.qriz.app.core.ui.test.TestTimeType
import com.qriz.app.feature.base.extention.collectSideEffect

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel(),
    moveToBack: () -> Unit,
    moveToGuide: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            PreviewUiEffect.MoveToGuide -> moveToGuide()
            PreviewUiEffect.MoveToBack -> moveToBack()
            is PreviewUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    TestScreen(
        questions = uiState.questions,
        testTimeType = TestTimeType.TOTAL,
        progressPercent = uiState.progressPercent,
        remainTimeText = uiState.remainTimeText,
        currentIndex = uiState.currentIndex,
        isLoading = uiState.isLoading,
        onSelectOption = { id, option ->
            viewModel.process(PreviewUiAction.SelectOption(questionID = id, option = option))
        },
        onClickNextPage = { viewModel.process(PreviewUiAction.ClickNextPage) },
        onClickSubmit = { viewModel.process(PreviewUiAction.ClickSubmit) },
        onClickPreviousPage = { viewModel.process(PreviewUiAction.ClickPreviousPage) },
        onClickCancel = { viewModel.process(PreviewUiAction.ClickCancel) },
    )
}

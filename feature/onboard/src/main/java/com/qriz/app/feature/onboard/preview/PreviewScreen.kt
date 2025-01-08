package com.qriz.app.feature.onboard.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.ui.test.TestScreen
import com.qriz.app.core.ui.test.TestTimeType
import com.qriz.app.feature.base.extention.collectSideEffect

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel(),
    moveToBack: () -> Unit,
    moveToGuide: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            PreviewUiEffect.MoveToGuide -> moveToGuide()
            PreviewUiEffect.MoveToBack -> moveToBack()
        }
    }

    TestScreen(
        questions = state.questions,
        selectedOptions = state.selectedOptions,
        testTimeType = TestTimeType.TOTAL,
        progressPercent = state.progressPercent,
        remainTimeText = state.remainTimeText,
        currentPage = state.currentPage,
        canTurnNextPage = state.canTurnNextPage,
        onOptionSelect = { id, answer ->
            viewModel.process(PreviewUiAction.SelectOption(questionID = id, option = answer))
        },
        onNextPage = { viewModel.process(PreviewUiAction.ClickNextPage) },
        onPreviousPage = { viewModel.process(PreviewUiAction.ClickPreviousPage) },
        onCancel = { viewModel.process(PreviewUiAction.ClickCancel) },
    )
}

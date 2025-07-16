package com.qriz.app.feature.mock_test.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mock_test.R
import com.qriz.app.feature.mock_test.sessions.model.Filter
import com.qriz.app.feature.mock_test.sessions.model.SessionState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MockTestSessionsScreen(
    viewModel: MockTestSessionsViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is MockTestSessionsUiEffect.ShowSnackBar -> onShowSnackbar(effect.message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.process(MockTestSessionsUiAction.LoadData)
    }

    MockTestSessionsContent(
        uiState = uiState.sessionState,
        onBack = onBack,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
private fun MockTestSessionsContent(
    uiState: SessionState,
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        QrizTopBar(
            navigationType = NavigationType.CLOSE,
            title = stringResource(R.string.mock_test),
            onNavigationClick = onBack,
        )

        when(uiState) {
            is SessionState.Success -> {

            }
            is SessionState.Failure -> {

            }
            is SessionState.Loading -> {

            }
        }
    }
}

@Composable
private fun SessionContent(
    filter: Filter,
    data: ImmutableList<MockTestSession>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

    }
}

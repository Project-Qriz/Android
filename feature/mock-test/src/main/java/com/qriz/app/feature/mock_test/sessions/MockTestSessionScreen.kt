package com.qriz.app.feature.mock_test.sessions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.ui.common.const.ErrorScreen
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
        sessionState = uiState.sessionState,
        onBack = onBack,
        filter = uiState.filter,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
private fun MockTestSessionsContent(
    sessionState: SessionState,
    filter: Filter,
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

        when(sessionState) {
            is SessionState.Success -> SessionContent(
                filter = filter,
                data = sessionState.data,
            )

            is SessionState.Failure -> ErrorScreen(
                title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
                description = sessionState.message,
                onClickRetry = {}
            )

            is SessionState.Loading -> QrizLoading()
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
        //TODO: Filter
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = data,
                key = { it.session }
            ) {
                //TODO: Items
            }
        }
    }
}

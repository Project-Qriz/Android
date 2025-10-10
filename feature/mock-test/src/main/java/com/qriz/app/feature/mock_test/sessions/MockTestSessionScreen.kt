package com.qriz.app.feature.mock_test.sessions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.data.mock_test.mock_test_api.model.MockTestSession
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.mock_test.R
import com.qriz.app.feature.mock_test.sessions.component.MockTestSessionCard
import com.qriz.app.feature.mock_test.sessions.component.MockTestSessionsFilterDropDownMenu
import com.qriz.app.feature.mock_test.sessions.model.SessionState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MockTestSessionsScreen(
    viewModel: MockTestSessionsViewModel = hiltViewModel(),
    onShowSnackbar: (String) -> Unit,
    moveToMockTest: (Long) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is MockTestSessionsUiEffect.ShowSnackBar -> onShowSnackbar(effect.message)
            is MockTestSessionsUiEffect.MoveToMockTest -> moveToMockTest(effect.id)
        }
    }

    MockTestSessionsContent(
        sessionState = uiState.sessionState,
        onBack = onBack,
        filter = uiState.filter,
        expandFilter = uiState.expandFilter,
        onClickFilter = { viewModel.process(MockTestSessionsUiAction.ClickSessionFilter) },
        onFilterSelected = { viewModel.process(MockTestSessionsUiAction.SelectSessionFilter(it)) },
        onClickCard = { viewModel.process(MockTestSessionsUiAction.ClickMockTest(it)) }
    )
}

@Composable
private fun MockTestSessionsContent(
    sessionState: SessionState,
    expandFilter: Boolean,
    filter: SessionFilter,
    onBack: () -> Unit,
    onClickFilter: () -> Unit,
    onFilterSelected: (SessionFilter) -> Unit,
    onClickCard: (Long) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        QrizTopBar(
            navigationType = NavigationType.CLOSE,
            title = stringResource(R.string.mock_test),
            onNavigationClick = onBack,
        )

        when (sessionState) {
            is SessionState.Success -> SessionContent(
                expandFilter = expandFilter,
                filter = filter,
                data = sessionState.data,
                onClickFilter = onClickFilter,
                onFilterSelected = onFilterSelected,
                onClickCard = onClickCard,
            )

            is SessionState.Failure -> ErrorScreen(
                title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
                description = sessionState.message,
                onClickRetry = {},
            )

            is SessionState.Loading -> QrizLoading()
        }
    }
}

@Composable
private fun SessionContent(
    expandFilter: Boolean,
    filter: SessionFilter,
    data: ImmutableList<MockTestSession>,
    onClickFilter: () -> Unit,
    onFilterSelected: (SessionFilter) -> Unit,
    onClickCard: (Long) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MockTestSessionsFilterDropDownMenu(
            modifier = Modifier.padding(24.dp),
            expand = expandFilter,
            sessionsFilter = filter,
            onClickFilter = onClickFilter,
            onFilterSelected = onFilterSelected,
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                bottom = 32.dp,
            )
        ) {
            items(items = data) {
                MockTestSessionCard(
                    totalScore = it.totalScore,
                    title = it.session,
                    completed = it.completed,
                    onCardClick = { onClickCard(it.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MockTestSessionsContentPreview() {
    QrizTheme {
        MockTestSessionsContent(
            sessionState = SessionState.Success(
                data = persistentListOf(
                    MockTestSession(
                        session = "12회차",
                        totalScore = 87,
                        completed = true,
                        id = 1,
                    ),
                    MockTestSession(
                        session = "11회차",
                        totalScore = 65,
                        completed = false,
                        id = 2,
                    ),
                    MockTestSession(
                        session = "10회차",
                        totalScore = 97,
                        completed = true,
                        id = 3,
                    ),
                )
            ),
            expandFilter = false,
            filter = SessionFilter.ALL,
            onBack = {},
            onClickFilter = {},
            onFilterSelected = {},
            onClickCard = {},
        )
    }
}

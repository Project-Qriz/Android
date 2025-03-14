package com.qriz.app.feature.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.home.component.TestScheduleCard
import com.qriz.app.feature.home.component.TestStartCard
import com.qriz.app.feature.home.component.TodayStudyCardPager
import com.qriz.app.feature.home.component.WeeklyCustomConcept
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.NOT_STARTED
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is HomeUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    HomeContent(
        userName = uiState.user.name,
        previewTestStatus = uiState.user.previewTestStatus,
        currentTodayStudyDay = uiState.currentTodayStudyDay,
        todayStudyConcepts = uiState.todayStudyConcepts,
        onInit = { viewModel.process(HomeUiAction.ObserveClient) },
        onClickTestDateChange = { },
        onClickTestDateRegister = {},
        onClickMockTest = {},
        onClickPreviewTest = { },
        onClickTodayStudyInit = {},
        onChangeTodayStudyCard = { viewModel.process(HomeUiAction.ChangeTodayStudyCard(it)) },
        onClickWeeklyCustomConcept = {},
    )
}

@Composable
fun HomeContent(
    userName: String,
    previewTestStatus: PreviewTestStatus,
    currentTodayStudyDay: Int,
    todayStudyConcepts: List<Int>,
    onInit: () -> Unit,
    onClickTestDateChange: () -> Unit,
    onClickTestDateRegister: () -> Unit,
    onClickMockTest: () -> Unit,
    onClickPreviewTest: () -> Unit,
    onClickTodayStudyInit: () -> Unit,
    onChangeTodayStudyCard: (Int) -> Unit,
    onClickWeeklyCustomConcept: () -> Unit,
) {
    Log.d("로그", "HomeContent: userName : $userName")
    val isInitialized = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isInitialized.value.not()) {
            onInit()
            isInitialized.value = true
        }
    }

    val horizontalPadding = remember { 18.dp }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(DSR.drawable.qriz_app_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(end = 8.dp)
                    .width(32.dp)
                    .height(32.dp),
            )
            Image(
                painter = painterResource(DSR.drawable.qriz_text_logo_white),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Black),
                modifier = Modifier
                    .width(62.dp)
                    .height(21.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            TestScheduleCard(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .padding(
                        top = 24.dp,
                        bottom = 32.dp
                    ),
                isExistSchedule = true,
                userName = userName,
                examDateString = "3월9일(토)",
                examPeriodString = "01.29(월) 10:00 ~ 02.02(금) 18:00",
                onClickTestDateChange = onClickTestDateChange,
                onClickTestDateRegister = onClickTestDateRegister,
            )

            TestStartCard(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .padding(bottom = 32.dp),
                isNeedPreviewTest = previewTestStatus.isNeedPreviewTest(),
                onClickMockTest = onClickMockTest,
                onClickPreviewTest = onClickPreviewTest
            )

            TodayStudyCardPager(
                horizontalPadding = horizontalPadding,
                isNeedPreviewTest = previewTestStatus.isNeedPreviewTest(),
                currentDay = currentTodayStudyDay,
                todayStudyConcepts = todayStudyConcepts,
                onClickInit = onClickTodayStudyInit,
                onChangeTodayStudyCard = onChangeTodayStudyCard
            )

            WeeklyCustomConcept(
                isNeedPreviewTest = previewTestStatus.isNeedPreviewTest(),
                onClick = onClickWeeklyCustomConcept,
            )

        }
    }
}


@Preview(showBackground = true, heightDp = 1500)
@Composable
fun HomeContentPreview() {
    QrizTheme {
        HomeContent(
            userName = "Qriz",
            previewTestStatus = NOT_STARTED,
            currentTodayStudyDay = 0,
            todayStudyConcepts = List(30) { it + 1 },
            onInit = {},
            onClickTestDateChange = {},
            onClickTestDateRegister = {},
            onClickPreviewTest = {},
            onClickMockTest = {},
            onClickTodayStudyInit = {},
            onChangeTodayStudyCard = {},
            onClickWeeklyCustomConcept = {},
        )
    }
}

package com.qriz.app.core.ui.common.const

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.application.application_api.model.Schedule
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDateTime

@Stable
sealed interface ExamScheduleState {
    @Immutable
    data object Loading : ExamScheduleState

    @Immutable
    data class Success(val data: ImmutableList<Schedule>) : ExamScheduleState

    @Immutable
    data class Error(val message: String) : ExamScheduleState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScheduleBottomSheet(
    schedulesLoadState: ExamScheduleState,
    onSelectExamDate: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    onClickRetry: () -> Unit = {},
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = White,
        onDismissRequest = onDismissRequest,
        tonalElevation = 0.dp,
        dragHandle = null,
    ) {
        ExamScheduleBottomSheetContent(
            schedulesLoadState = schedulesLoadState,
            onSelectExamDate = onSelectExamDate,
            onClickRetry = onClickRetry,
        )
    }
}

@Composable
private fun ExamScheduleBottomSheetContent(
    schedulesLoadState: ExamScheduleState,
    onSelectExamDate: (Long) -> Unit,
    onClickRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            )
        )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 11.dp,
                    bottom = 6.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(47.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Gray200)
            )
        }

        Text(
            text = "시험 등록",
            style = QrizTheme.typography.heading2,
            color = Gray800,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 18.dp,
                    end = 18.dp,
                    bottom = 8.dp
                )
        )
        when (schedulesLoadState) {
            ExamScheduleState.Loading -> Loading()

            is ExamScheduleState.Success -> {
                if (schedulesLoadState.data.isNotEmpty()) {
                    ScheduleContent(
                        data = schedulesLoadState.data,
                        onSelect = onSelectExamDate,
                    )
                } else {
                    EmptyContent()
                }
            }

            is ExamScheduleState.Error -> {
                ErrorScreen(
                    title = schedulesLoadState.message,
                    description = stringResource(R.string.try_again),
                    onClickRetry = onClickRetry,
                )
            }
        }
    }
}

@Composable
private fun ScheduleContent(
    data: ImmutableList<Schedule>,
    onSelect: (Long) -> Unit,
) {
    LazyColumn {
        items(
            count = data.size,
            key = { index -> data[index].applicationId },
        ) {
            val item = data[it]

            val currentTimeMillis = System.currentTimeMillis()
            val isPeriodExpired = item.periodStartEpochMilli > currentTimeMillis || item.periodEndEpochMilli < currentTimeMillis

            TestDateItem(
                isChecked = item.applicationId == item.userApplyId,
                isPeriodExpired = isPeriodExpired,
                name = item.examName,
                period = item.period,
                date = item.examDate,
                onClick = { onSelect(item.applicationId) },
            )
            if (it < data.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Gray100,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_timer),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.schedule_is_empty),
                style = QrizTheme.typography.heading2,
                color = Black,
            )
            Text(
                text = stringResource(R.string.schedule_is_not_registered),
                style = QrizTheme.typography.body2,
                color = Gray500,
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TestDateItem(
    name: String,
    period: String,
    date: String,
    isPeriodExpired: Boolean = false,
    isChecked: Boolean,
    onClick: () -> Unit = {},
) {
    val textColor = if (isPeriodExpired) Gray300 else Gray800
    Box(
        modifier = Modifier
            .background(if (isPeriodExpired) Gray100 else White)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    enabled = isPeriodExpired.not(),
                    onClick = onClick
                )
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExamCheckBox(
                size = 24.dp,
                isChecked = isChecked,
                modifier = Modifier.padding(end = 16.dp),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = name,
                    style = QrizTheme.typography.headline2,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = stringResource(
                        R.string.exam_application_period,
                        period
                    ),
                    style = QrizTheme.typography.body2Long,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(
                        R.string.exam_date,
                        date
                    ),
                    style = QrizTheme.typography.body2Long,
                    color = textColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateItemPreview() {
    QrizTheme {
        TestDateItem(
            isChecked = false,
            isPeriodExpired = false,
            name = "Test Name",
            period = "01.29(월) 10:00 ~ 02.02(금) 18:00",
            date = "3월9일(토)",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateItemPreview2() {
    QrizTheme {
        TestDateItem(
            isChecked = true,
            isPeriodExpired = false,
            name = "Test Name",
            period = "01.29(월) 10:00 ~ 02.02(금) 18:00",
            date = "3월9일(토)",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateItemPreview3() {
    QrizTheme {
        TestDateItem(
            isPeriodExpired = true,
            isChecked = false,
            name = "Test Name",
            period = "01.29(월) 10:00 ~ 02.02(금) 18:00",
            date = "3월9일(토)",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateBottomSheetContentPreview() {
    QrizTheme {
        ExamScheduleBottomSheetContent(
            schedulesLoadState = ExamScheduleState.Success(
                persistentListOf(
                    Schedule(
                        applicationId = 1,
                        examName = "Test Name",
                        period = "01.29(월) 10:00 ~ 02.02(금) 18:00",
                        examDate = "3월9일(토)",
                        userApplyId = null,
                        releaseDate = "",
                        periodStart = LocalDateTime.now(),
                        periodEnd = LocalDateTime.now(),
                    )
                )
            ),
            onSelectExamDate = {},
            onClickRetry = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateBottomSheetEmptyContentPreview() {
    QrizTheme {
        ExamScheduleBottomSheetContent(
            schedulesLoadState = ExamScheduleState.Success(persistentListOf()),
            onSelectExamDate = {},
            onClickRetry = {},
        )
    }
}

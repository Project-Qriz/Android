package com.qriz.app.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.application.application_api.model.DdayType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.home.R

@Composable
fun ExamScheduleCard(
    modifier: Modifier = Modifier,
    userName: String,
    scheduleState: UserExamUiState,
    onClickApply: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        if (scheduleState is UserExamUiState.Scheduled) DDayHeader(
            userName = userName,
            ddayCount = scheduleState.dday,
        )

        QrizCard {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                when(scheduleState) {
                    is UserExamUiState.NoSchedule -> {
                        Text(
                            text = stringResource(
                                R.string.exam_register_message,
                                userName
                            ),
                            style = QrizTheme.typography.heading1,
                            color = Gray800
                        )
                    }

                    is UserExamUiState.PastExam -> {
                        Text(
                            text = stringResource(R.string.exam_date_expired),
                            style = QrizTheme.typography.heading1,
                            color = Gray800
                        )
                    }

                    is UserExamUiState.Scheduled -> {
                        Text(
                            text = stringResource(R.string.exam_date,  scheduleState.examDate),
                            style = QrizTheme.typography.headline1,
                            color = Gray800,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = scheduleState.examName,
                            style = QrizTheme.typography.subhead,
                            color = Gray500,
                        )

                        Text(
                            text = stringResource(R.string.exam_application_period, scheduleState.examPeriod),
                            style = QrizTheme.typography.subhead,
                            color = Gray500,
                        )
                    }
                }

                HorizontalDivider(
                    color = Blue100,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                when(scheduleState) {
                    is UserExamUiState.NoSchedule -> {
                        Text(
                            text = stringResource(R.string.no_registered_schedule),
                            style = QrizTheme.typography.headline1,
                            color = Gray800,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = stringResource(R.string.register_schedule_now),
                            style = QrizTheme.typography.body2,
                            color = Gray500,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    is UserExamUiState.PastExam -> {
                        Text(
                            text = stringResource(R.string.re_register_schedule),
                            style = QrizTheme.typography.headline1,
                            color = Gray800,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = stringResource(R.string.register_schedule_now),
                            style = QrizTheme.typography.body2,
                            color = Gray500,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    is UserExamUiState.Scheduled -> {
                        Text(
                            text = stringResource(R.string.change_schedule_prompt),
                            style = QrizTheme.typography.subhead,
                            color = Gray500,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                val buttonText = if (scheduleState.isExistSchedule) stringResource(R.string.change)
                else stringResource(R.string.register)

                QrizButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    text = buttonText,
                    onClick = onClickApply,
                    textColor = Gray800,
                    containerColor = White,
                    strokeColor = Gray200,
                    textStyle = QrizTheme.typography.label
                )
            }
        }
    }
}

@Composable
private fun DDayHeader(
    userName: String,
    ddayCount: Int,
) {
    Text(
        text = stringResource(
            R.string.exam_countdown_message,
            userName
        ),
        style = QrizTheme.typography.heading1,
        color = Gray800,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    Row(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QrizCard(
            color = Blue500,
            cornerRadius = 8.dp,
            elevation = 0.dp
        ) {
            Text(
                text = "D",
                color = White,
                style = QrizTheme.typography.title2,
                modifier = Modifier.padding(
                        vertical = 8.dp, // TODO:  다시 체크
                        horizontal = 16.dp
                    )
            )
        }

        Text(
            text = "-",
            style = QrizTheme.typography.title2,
            color = Blue500,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        QrizCard(
            color = Blue500,
            cornerRadius = 8.dp,
            elevation = 0.dp
        ) {
            Text(
                text = ddayCount.toString(),
                color = White,
                style = QrizTheme.typography.title2,
                modifier = Modifier.padding(
                        vertical = 8.dp, // TODO:  다시 체크
                        horizontal = 16.dp
                    )
            )
        }
    }
}

@Stable
sealed interface UserExamUiState {
    val isExistSchedule: Boolean

    @Immutable
    data object NoSchedule : UserExamUiState {
        override val isExistSchedule: Boolean = false
    }

    @Immutable
    data class Scheduled(
        val examName: String,
        val examDate: String,
        val examPeriod: String,
        val dday: Int,
        val ddayType: DdayType,
    ) : UserExamUiState {
        override val isExistSchedule: Boolean = true
    }

    @Immutable
    data object PastExam : UserExamUiState {
        override val isExistSchedule: Boolean = false
    }
}

@Preview(showBackground = true)
@Composable
private fun NoSchedulePreview() {
    QrizTheme {
        ExamScheduleCard(
            userName = "Qriz",
            scheduleState = UserExamUiState.NoSchedule,
            onClickApply = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduledPreview() {
    QrizTheme {
        ExamScheduleCard(
            userName = "Qriz",
            scheduleState = UserExamUiState.Scheduled(
                examName = "SQL 개발자 자격시험",
                examDate = "3월 9일 (토)",
                examPeriod = "01.29 (월) 10:00 ~ 02.02 (금) 18:00",
                dday = 29,
                ddayType = DdayType.BEFORE
            ),
            onClickApply = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PassedPreview() {
    QrizTheme {
        ExamScheduleCard(
            userName = "Qriz",
            scheduleState = UserExamUiState.PastExam,
            onClickApply = {},
        )
    }
}


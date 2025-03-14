package com.qriz.app.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun TestScheduleCard(
    modifier: Modifier = Modifier,
    userName: String,
    examDateString: String,
    examPeriodString: String,
    isExistSchedule: Boolean,
    onClickTestDateChange: () -> Unit,
    onClickTestDateRegister: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        if (isExistSchedule) DDayHeader(userName = userName)

        QrizCard {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                if (isExistSchedule) {
                    Text(
                        text = stringResource(R.string.exam_date) + examDateString,
                        style = QrizTheme.typography.headline1,
                        color = Gray800,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = "제 52회 SQL 개발자",
                        style = QrizTheme.typography.subhead,
                        color = Gray500,
                    )
                    Text(
                        text = stringResource(R.string.exam_application_period) + examPeriodString,
                        style = QrizTheme.typography.subhead,
                        color = Gray500,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.exam_register_message, userName),
                        style = QrizTheme.typography.heading1,
                        color = Gray800

                    )
                }

                HorizontalDivider(
                    color = Blue100,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                if (isExistSchedule) {
                    Text(
                        text = stringResource(R.string.change_schedule_prompt),
                        style = QrizTheme.typography.subhead,
                        color = Gray500,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.no_registered_schedule),
                        style = QrizTheme.typography.headline1,
                        color = Gray800,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                    )
                    Text(
                        text = stringResource(R.string.register_schedule_now),
                        style = QrizTheme.typography.body2,
                        color = Gray500,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    )
                }

                val buttonText =
                    if (isExistSchedule) stringResource(R.string.change)
                    else stringResource(R.string.register)

                QrizButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    text = buttonText,
                    onClick = if (isExistSchedule) onClickTestDateChange else onClickTestDateRegister,
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
    userName: String
) {
    Text(
        text = stringResource(R.string.exam_countdown_message, userName),
        style = QrizTheme.typography.heading1,
        color = Gray800,
        modifier = Modifier
            .padding(bottom = 12.dp)
    )
    Row(
        modifier = Modifier
            .padding(bottom = 20.dp),
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
                modifier = Modifier
                    .padding(
                        vertical = 8.dp, // TODO:  다시 체크
                        horizontal = 16.dp
                    )
            )
        }

        Text(
            text = "-",
            style = QrizTheme.typography.title2,
            color = Blue500,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )

        QrizCard(
            color = Blue500,
            cornerRadius = 8.dp,
            elevation = 0.dp
        ) {
            Text(
                text = "29",
                color = White,
                style = QrizTheme.typography.title2,
                modifier = Modifier
                    .padding(
                        vertical = 8.dp, // TODO:  다시 체크
                        horizontal = 16.dp
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestScheduleCardPreview() {
    QrizTheme {
        TestScheduleCard(
            userName = "Qriz",
            examDateString = "3월9일(토)",
            examPeriodString = "01.29(월) 10:00 ~ 02.02(금) 18:00",
            isExistSchedule = true,
            onClickTestDateChange = {},
            onClickTestDateRegister = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestScheduleCardPreview2() {
    QrizTheme {
        TestScheduleCard(
            userName = "Qriz",
            examDateString = "3월9일(토)",
            examPeriodString = "01.29(월) 10:00 ~ 02.02(금) 18:00",
            isExistSchedule = false,
            onClickTestDateChange = {},
            onClickTestDateRegister = {},
        )
    }
}


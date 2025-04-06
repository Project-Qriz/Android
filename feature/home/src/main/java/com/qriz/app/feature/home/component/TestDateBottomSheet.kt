package com.qriz.app.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizRadioButton
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDateBottomSheet(
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = null,
    ) {
        TestDateBottomSheetContent()
    }
}

@Composable
fun TestDateBottomSheetContent() {
    val scrollState = rememberScrollState()
    val checkedButtonId = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .clip(
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
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {

            for (i in 1..20) {
                TestDateItem(
                    num = i,
                    isPeriodExpired = i < 3,
                    isChecked = i == checkedButtonId.value,
                    onClick = { checkedButtonId.value = i }
                )
            }
        }
    }
}

@Composable
private fun TestDateItem(
    num: Int = 0,
    isPeriodExpired: Boolean = false,
    isChecked: Boolean,
    onClick: () -> Unit = {}
) {
    val textColor = if (isPeriodExpired) Gray300 else Gray800
    Box(
        modifier = Modifier
            .background(if (isPeriodExpired) Gray100 else White)
    ) {
        //기간이 지난 경우 enable = false, 회색 배경 처리
        Row(
            modifier = Modifier
                .clickable(
                    enabled = isPeriodExpired.not(),
                    onClick = onClick
                )
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QrizRadioButton(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 16.dp),
                selected = isChecked,
                enabled = isPeriodExpired.not(),
                onClick = onClick,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "제 52회 SQL 개발자$num",
                    style = QrizTheme.typography.headline2,
                    color = textColor,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "접수기간: 01.29(월) 10:00 ~ 02.02(금) 18:00",
                    style = QrizTheme.typography.body2Long,
                    color = textColor,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "시험일: 3월9일(토)",
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
            isChecked = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateItemPreview2() {
    QrizTheme {
        TestDateItem(
            isChecked = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateItemPreview3() {
    QrizTheme {
        TestDateItem(
            isPeriodExpired = true,
            isChecked = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestDateBottomSheetContentPreview() {
    QrizTheme {
        TestDateBottomSheetContent()
    }
}

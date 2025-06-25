package com.qriz.app.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.home.R
import com.qriz.app.core.designsystem.R as DSR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyStudyPlanDayFilterBottomSheet(
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    onClickToday: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        containerColor = White,
        onDismissRequest = onDismissRequest,
        dragHandle = null,
    ) {
        DailyStudyPlanDayFilterContent(
            selectedDay = selectedDay,
            onSelectDay = onSelectDay,
            onClickToday = onClickToday,
        )
    }
}

@Composable
private fun DailyStudyPlanDayFilterContent(
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    onClickToday: () -> Unit,
) {
    var showingWeek by remember { mutableIntStateOf((selectedDay - 1) / 7 + 1) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            vertical = 12.dp,
            horizontal = 18.dp
        )
    ) {
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(47.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Gray200)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_day),
                style = QrizTheme.typography.headline3.copy(color = Gray800)
            )

            TodayButton(onClick = onClickToday)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(
                    R.string.week,
                    showingWeek
                ),
                style = QrizTheme.typography.body2.copy(color = Gray600),
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {
                if (showingWeek > 1) showingWeek--
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(DSR.drawable.ic_keyboard_arrow_left),
                    contentDescription = null,
                    tint = if (showingWeek == 1) Gray200 else Gray800
                )
            }

            IconButton(onClick = {
                if (showingWeek < 5) showingWeek++
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(DSR.drawable.ic_keyboard_arrow_right),
                    contentDescription = null,
                    tint = if (showingWeek == 5) Gray200 else Gray800
                )
            }
        }

        WeekDays(
            selectedDay = selectedDay,
            showingWeek = showingWeek,
            onSelectDay = onSelectDay,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun WeekDays(
    modifier: Modifier = Modifier,
    selectedDay: Int,
    showingWeek: Int,
    onSelectDay: (Int) -> Unit,
) {
    val startDay = showingWeek * 7 - 6
    val days = if (showingWeek < 5) (startDay..startDay + 6).toList() //최대 30일로 5주차까지 가능
    else (startDay..startDay + 1).toList()

    Layout(
        modifier = modifier,
        content = {
            days.forEach { day ->
                DayItem(
                    day = day,
                    isSelected = day == selectedDay,
                    onClick = { onSelectDay(day) },
                )
            }
        },
    ) { measurables, constraints ->
        val spacing = 8.dp.roundToPx()
        val itemWidth = (constraints.maxWidth - spacing * 3) / 4

        val itemHeight = measurables.maxOfOrNull { measurable ->
            measurable.minIntrinsicHeight(itemWidth)
        } ?: 36.dp.roundToPx()

        val placeables = measurables.map { measurable ->
            measurable.measure(
                Constraints.fixed(
                    width = itemWidth,
                    height = itemHeight,
                )
            )
        }

        val rowCount = 2
        val totalHeight = rowCount * itemHeight + spacing

        layout(constraints.maxWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                val row = index / 4
                val column = index % 4

                placeable.placeRelative(
                    x = column * itemWidth + (column * spacing),
                    y = row * (itemHeight + spacing)
                )
            }
        }
    }
}

@Composable
private fun DayItem(
    modifier: Modifier = Modifier,
    day: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) Black else Gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = 8.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                R.string.day,
                day
            ),
            style = QrizTheme.typography.body2.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            )
        )
    }
}

@Composable
private fun TodayButton(
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .border(
            width = 1.dp,
            color = Gray200,
            shape = RoundedCornerShape(4.dp)
        )
        .padding(
            horizontal = 10.dp,
            vertical = 4.dp
        )
        .clickable { onClick() }) {
        Text(
            text = stringResource(R.string.today),
            style = QrizTheme.typography.caption.copy(color = Gray600),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DailyStudyPlanDayFilterBottomSheetPreview() {
    QrizTheme {
        DailyStudyPlanDayFilterContent(
            selectedDay = 8,
            onSelectDay = {},
            onClickToday = {},
        )
    }
}

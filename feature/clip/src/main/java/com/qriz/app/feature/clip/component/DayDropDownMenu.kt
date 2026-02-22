package com.qriz.app.feature.clip.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.qriz.app.core.designsystem.theme.Blue200
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.designsystem.R as DSR

private const val HEADER_TEXT = "회차 선택"

@Composable
fun DayDropDownMenu(
    modifier: Modifier = Modifier,
    selectedDay: String,
    days: List<String>,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    onSelectDay: (String) -> Unit = {},
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current.containerSize
    val maxDropdownHeight = with(density) { (windowInfo.height * 232f / 812f).toDp() }
    val popupMargin = with(density) { 10.dp.toPx() }.toInt()

    var triggerHeightPx by remember { mutableIntStateOf(0) }
    var triggerWidthPx by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(8.dp))
                .background(White, RoundedCornerShape(8.dp))
                .onGloballyPositioned {
                    triggerHeightPx = it.size.height
                    triggerWidthPx = it.size.width
                }
                .clickable { onExpandedChanged(!isExpanded) }
                .padding(horizontal = 12.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedDay.ifEmpty { HEADER_TEXT },
                style = QrizTheme.typography.subhead,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(
                    if (isExpanded) DSR.drawable.ic_keyboard_arrow_up
                    else DSR.drawable.arrow_down_icon
                ),
                contentDescription = null,
                tint = Gray600,
            )
        }

        if (isExpanded) {
            Popup(
                offset = IntOffset(x = 0, y = triggerHeightPx + popupMargin),
                onDismissRequest = { onExpandedChanged(false) },
            ) {
                DayDropDownContent(
                    dropdownWidth = with(density) { triggerWidthPx.toDp() },
                    selectedDay = selectedDay,
                    days = days,
                    maxScrollableHeight = maxDropdownHeight,
                    onSelect = { day ->
                        onSelectDay(day)
                        onExpandedChanged(false)
                    },
                )
            }
        }
    }
}

@Composable
private fun DayDropDownContent(
    dropdownWidth: Dp,
    selectedDay: String,
    days: List<String>,
    maxScrollableHeight: Dp,
    onSelect: (String) -> Unit,
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val isScrollable by remember { derivedStateOf { scrollState.maxValue > 0 } }
    var scrollableAreaHeightPx by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier.width(dropdownWidth),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 3.dp,
        color = White,
    ) {
        Column {
            // 고정 헤더 - 스크롤 영역 밖
            DayDropDownItem(
                text = HEADER_TEXT,
                isSelected = selectedDay.isEmpty(),
                onClick = { onSelect("") },
            )

            // 스크롤 가능한 아이템 영역
            Box(
                modifier = Modifier
                    .heightIn(max = maxScrollableHeight)
                    .onSizeChanged { scrollableAreaHeightPx = it.height },
            ) {
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    days.forEach { day ->
                        DayDropDownItem(
                            text = day,
                            isSelected = day == selectedDay,
                            onClick = { onSelect(day) },
                        )
                    }
                }

                if (isScrollable) {
                    val thumbOffsetPx by remember {
                        derivedStateOf {
                            val thumbHeightPx = with(density) { 16.dp.toPx() }
                            if (scrollState.maxValue > 0) {
                                (scrollState.value.toFloat() / scrollState.maxValue) *
                                    (scrollableAreaHeightPx - thumbHeightPx)
                            } else 0f
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 4.dp)
                            .offset { IntOffset(0, thumbOffsetPx.toInt()) }
                            .width(4.dp)
                            .height(16.dp)
                            .background(Blue400, RoundedCornerShape(2.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun DayDropDownItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected) Blue200 else Color.Transparent,
            )
            .padding(horizontal = 8.dp, vertical = 16.dp),
    ) {
        Text(
            text = text,
            style = QrizTheme.typography.subhead,
        )
    }
}

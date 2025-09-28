package com.qriz.app.feature.mock_test.result.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.mock_test.result.MockTestResultUiState

@Composable
fun HistoryFilter(
    modifier: Modifier = Modifier,
    showDropDown: Boolean,
    selected: MockTestResultUiState.HistoricalScoreFilter,
    filters: List<MockTestResultUiState.HistoricalScoreFilter>,
    onClickFilter: () -> Unit,
    onSelect: (MockTestResultUiState.HistoricalScoreFilter) -> Unit,
) {
    val density = LocalDensity.current

    var dropDownState by remember {
        mutableStateOf(
            DropDownMenuState(
                offset = Offset.Zero,
                size = IntSize.Zero,
            )
        )
    }

    val popUpEndPadding =
        remember(dropDownState) { with(density) { 123.dp.toPx() - dropDownState.size.width }.toInt() }

    val margin = with(LocalDensity.current) { 8.dp.toPx() }.toInt()

    Box {
        Row(
            modifier = modifier
                .clickable(onClick = onClickFilter)
                .onGloballyPositioned {
                    dropDownState = DropDownMenuState(
                        offset = it.positionInParent(),
                        size = it.size
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = selected.value,
                style = QrizTheme.typography.headline3.copy(color = Gray600),
            )
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_down),
                contentDescription = null,
                tint = Gray600
            )
        }

        if (showDropDown) {
            Popup(
                offset = IntOffset(
                    x = dropDownState.offset.x.toInt() - popUpEndPadding,
                    y = dropDownState.offset.y.toInt() + dropDownState.size.height + margin,
                ),
                onDismissRequest = onClickFilter
            ) {
                HistoryFilterDropDownMenu(
                    filters = filters,
                    selected = selected,
                    onSelect = onSelect
                )
            }
        }
    }
}

@Composable
private fun HistoryFilterDropDownMenu(
    modifier: Modifier = Modifier,
    selected: MockTestResultUiState.HistoricalScoreFilter,
    filters: List<MockTestResultUiState.HistoricalScoreFilter>,
    onSelect: (MockTestResultUiState.HistoricalScoreFilter) -> Unit,
) {
    Surface(
        modifier = modifier.requiredWidth(123.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 3.dp,
        color = White,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            filters.forEach {
                HistoryFilterDropDownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    value = it,
                    isSelected = it == selected,
                    onSelectSubjectFilter = onSelect,
                )
            }
        }
    }
}

@Composable
private fun HistoryFilterDropDownMenuItem(
    modifier: Modifier = Modifier,
    value: MockTestResultUiState.HistoricalScoreFilter,
    isSelected: Boolean,
    onSelectSubjectFilter: (MockTestResultUiState.HistoricalScoreFilter) -> Unit,
) {
    Text(
        modifier = modifier
            .clickable { onSelectSubjectFilter(value) }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        text = value.value,
        style = QrizTheme.typography.body2.copy(
            color = if (isSelected) Blue500 else Gray600,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        ),
    )
}

internal data class DropDownMenuState(
    val offset: Offset,
    val size: IntSize,
)

@Preview(showBackground = true)
@Composable
private fun HistoryFilterPreview() {
    QrizTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.TopEnd,
        ) {
            HistoryFilter(
                showDropDown = true,
                selected = MockTestResultUiState.HistoricalScoreFilter.TOTAL,
                filters = MockTestResultUiState.HistoricalScoreFilter.entries,
                onClickFilter = {},
                onSelect = {},
            )
        }
    }
}

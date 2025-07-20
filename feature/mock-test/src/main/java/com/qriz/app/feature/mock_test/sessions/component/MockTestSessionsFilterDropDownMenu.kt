package com.qriz.app.feature.mock_test.sessions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.qriz.app.core.data.mock_test.mock_test_api.model.SessionFilter
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.mock_test.R
import com.qriz.app.core.designsystem.R as DSR

private fun matchSessionFilterTitleResId(filter: SessionFilter) = when(filter) {
    SessionFilter.ALL -> R.string.filter_total
    SessionFilter.COMPLETED -> R.string.filter_completed
    SessionFilter.NOT_COMPLETED -> R.string.filter_not_completed
    SessionFilter.OLDEST_FIRST -> R.string.filter_past_order
}

@Composable
fun MockTestSessionsFilterDropDownMenu(
    modifier: Modifier = Modifier,
    expand: Boolean,
    sessionsFilter: SessionFilter,
    onClickFilter: () -> Unit,
    onFilterSelected: (SessionFilter) -> Unit,
) {
    var boxOffset by remember { mutableStateOf(Offset.Zero) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Row(
        modifier = modifier
            .clickable(onClick = onClickFilter)
            .onGloballyPositioned {
                boxOffset = it.positionInParent()
                boxSize = it.size
            }
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
            ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(matchSessionFilterTitleResId(sessionsFilter)),
            style = QrizTheme.typography.headline3.copy(color = Gray600),
        )
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(DSR.drawable.ic_keyboard_arrow_down),
            contentDescription = null,
            tint = Gray600
        )
    }

    val margin = with(LocalDensity.current) { 8.dp.toPx() }.toInt()

    if (expand) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(
                x = boxOffset.x.toInt(),
                y = boxOffset.y.toInt() + boxSize.height + margin,
            )
        ) {
            FilterDropDownMenu(
                modifier = Modifier,
                selectedSessionFilter = sessionsFilter,
                onSelectSubjectFilter = onFilterSelected
            )
        }
    }
}

@Composable
private fun FilterDropDownMenu(
    modifier: Modifier = Modifier,
    selectedSessionFilter: SessionFilter,
    onSelectSubjectFilter: (SessionFilter) -> Unit,
) {
    Surface(
        modifier = modifier
            .requiredWidth(123.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 3.dp,
        color = White,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            SessionFilter.entries.forEach {
                FilterDropDownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    value = it,
                    isSelected = it == selectedSessionFilter,
                    onSelectSubjectFilter = onSelectSubjectFilter,
                )
            }
        }
    }
}

@Composable
private fun FilterDropDownMenuItem(
    modifier: Modifier = Modifier,
    value: SessionFilter,
    isSelected: Boolean,
    onSelectSubjectFilter: (SessionFilter) -> Unit,
) {
    Text(
        modifier = modifier
            .clickable { onSelectSubjectFilter(value) }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        text = stringResource(matchSessionFilterTitleResId(value)),
        style = QrizTheme.typography.body2.copy(
            color = if (isSelected) Blue500 else Gray600,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun MockTestSessionsFilterDropDownMenuPreview() {
    var expand by remember { mutableStateOf(true) }
    var selectedFilter by remember { mutableStateOf(SessionFilter.ALL) }

    MockTestSessionsFilterDropDownMenu(
        modifier = Modifier.padding(vertical = 16.dp),
        expand = expand,
        sessionsFilter = selectedFilter,
        onClickFilter = { expand = !expand },
        onFilterSelected = {
            selectedFilter = it
            expand = false
        }
    )
}

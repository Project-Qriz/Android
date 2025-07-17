package com.qriz.app.core.ui.test

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.model.ScoreDetailSubjectFilter
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun SubjectFilter(
    modifier: Modifier = Modifier,
    showDropDown: Boolean,
    subject: ScoreDetailSubjectFilter,
    onClickFilter: () -> Unit,
    onSelectSubjectFilter: (ScoreDetailSubjectFilter) -> Unit,
) {
    var dropDownState by remember {
        mutableStateOf(
            DropDownMenuState(
                offset = Offset.Zero,
                size = IntSize.Zero,
            )
        )
    }

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
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = subject.title,
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

        if (showDropDown) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(
                    x = dropDownState.offset.x.toInt(),
                    y = dropDownState.offset.y.toInt() + dropDownState.size.height + margin,
                )
            ) {
                SubjectFilterDropDownMenu(
                    modifier = Modifier,
                    selectedSubjectFilter = subject,
                    onSelectSubjectFilter = onSelectSubjectFilter
                )
            }
        }
    }
}

@Composable
private fun SubjectFilterDropDownMenu(
    modifier: Modifier = Modifier,
    selectedSubjectFilter: ScoreDetailSubjectFilter,
    onSelectSubjectFilter: (ScoreDetailSubjectFilter) -> Unit,
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
            ScoreDetailSubjectFilter.entries.forEach {
                SubjectFilterDropDownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    value = it,
                    isSelected = it == selectedSubjectFilter,
                    onSelectSubjectFilter = onSelectSubjectFilter,
                )
            }
        }
    }
}

@Composable
private fun SubjectFilterDropDownMenuItem(
    modifier: Modifier = Modifier,
    value: ScoreDetailSubjectFilter,
    isSelected: Boolean,
    onSelectSubjectFilter: (ScoreDetailSubjectFilter) -> Unit,
) {
    Text(
        modifier = modifier
            .clickable { onSelectSubjectFilter(value) }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        text = value.title,
        style = QrizTheme.typography.body2.copy(
            color = if (isSelected) Blue500 else Gray600,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        ),
    )
}

data class DropDownMenuState(
    val offset: Offset,
    val size: IntSize,
)

@Preview(showBackground = true)
@Composable
private fun SubjectFilterPreview() {
    QrizTheme {
        SubjectFilter(
            showDropDown = true,
            subject = ScoreDetailSubjectFilter.TOTAL,
            onClickFilter = {},
            onSelectSubjectFilter = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SubjectFilterDropDownMenuPreview() {
    QrizTheme {
        SubjectFilterDropDownMenu(
            selectedSubjectFilter = ScoreDetailSubjectFilter.TOTAL,
            onSelectSubjectFilter = {},
        )
    }
}



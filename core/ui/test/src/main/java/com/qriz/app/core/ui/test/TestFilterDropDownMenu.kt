package com.qriz.app.core.ui.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T> TestFilterDropDownMenu(
    modifier: Modifier = Modifier,
    expended: Boolean,
    state: DropDownMenuState,
    items: List<T>,
    onItemClick: (T) -> Unit,
    title: @Composable () -> Unit,
) {
    var dropDownState by remember {
        mutableStateOf(
            DropDownMenuState(
                offset = Offset.Zero,
                size = IntSize.Zero,
            )
        )
    }
    
    Box(
        modifier = modifier,
    ) {
        title()

        if (expended) {
            Popup() {  }
        }
    }
}

@Composable
private fun DropDownMenu(
    modifier: Modifier,
    
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
            DropDownItem(
                modifier = Modifier.fillMaxWidth(),
                value = it,
                isSelected = it == selectedSubjectFilter,
                onSelectSubjectFilter = onSelectSubjectFilter,
            )
        }
    }
}

@Composable
private fun DropDownItem(
    modifier: Modifier = Modifier,
    value: String,
    isSelected: Boolean,
    index: Int,
    onSelect: (Int) -> Unit,
) {
    Text(
        modifier = modifier
            .clickable { onSelect(index) }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        text = value,
        style = QrizTheme.typography.body2.copy(
            color = if (isSelected) Blue500 else Gray600,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        ),
    )
}

@Immutable
data class DropDownMenuState(
    val offset: Offset,
    val size: IntSize,
)

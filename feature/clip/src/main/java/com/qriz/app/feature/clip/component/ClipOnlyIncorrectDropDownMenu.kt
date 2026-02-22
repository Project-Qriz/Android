package com.qriz.app.feature.clip.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.clip.R
import com.qriz.app.core.designsystem.R as DSR

@Composable
internal fun ClipOnlyIncorrectDropDownMenu(
    modifier: Modifier = Modifier,
    expand: Boolean,
    onlyIncorrect: Boolean,
    onClickFilter: () -> Unit,
    onFilterSelected: (Boolean) -> Unit,
) {
    var boxOffset by remember { mutableStateOf(Offset.Zero) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier.onGloballyPositioned {
            boxOffset = it.positionInParent()
            boxSize = it.size
        }) {
        FilterButton(
            text = stringResource(if (onlyIncorrect) R.string.only_incorrect else R.string.all),
            contentColor = if (onlyIncorrect) White else Gray500,
            containerColor = if (onlyIncorrect) Gray700 else White,
            borderColor = if (onlyIncorrect) Gray700 else Gray200,
            icon = DSR.drawable.arrow_down_icon,
            onClick = onClickFilter,
        )
    }

    val margin = with(LocalDensity.current) { 8.dp.toPx() }.toInt()

    if (expand) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(
                x = boxOffset.x.toInt(),
                y = boxOffset.y.toInt() + boxSize.height + margin,
            ),
            properties = PopupProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
            ),
            onDismissRequest = { onClickFilter() }
        ) {
            OnlyIncorrectDropDownMenuContent(
                onlyIncorrect = onlyIncorrect,
                onFilterSelected = onFilterSelected,
            )
        }
    }
}

@Composable
private fun OnlyIncorrectDropDownMenuContent(
    modifier: Modifier = Modifier,
    onlyIncorrect: Boolean,
    onFilterSelected: (Boolean) -> Unit,
) {
    Surface(
        modifier = modifier.requiredWidth(100.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 3.dp,
        color = White,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            OnlyIncorrectDropDownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                titleResId = R.string.all,
                isSelected = !onlyIncorrect,
                onClick = { onFilterSelected(false) },
            )
            OnlyIncorrectDropDownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                titleResId = R.string.only_incorrect,
                isSelected = onlyIncorrect,
                onClick = { onFilterSelected(true) },
            )
        }
    }
}

@Composable
private fun OnlyIncorrectDropDownMenuItem(
    modifier: Modifier = Modifier,
    titleResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Text(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        text = stringResource(titleResId),
        style = QrizTheme.typography.body2.copy(
            color = if (isSelected) Blue500 else Gray600,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun ClipOnlyIncorrectDropDownMenuPreview() {
    var expand by remember { mutableStateOf(true) }
    var onlyIncorrect by remember { mutableStateOf(false) }

    QrizTheme {
        ClipOnlyIncorrectDropDownMenu(
            modifier = Modifier.padding(16.dp),
            expand = expand,
            onlyIncorrect = onlyIncorrect,
            onClickFilter = { expand = !expand },
            onFilterSelected = {
                onlyIncorrect = it
                expand = false
            })
    }
}

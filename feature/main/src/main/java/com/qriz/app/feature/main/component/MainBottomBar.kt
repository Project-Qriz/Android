package com.qriz.app.feature.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.main.MainTab
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    tabs: ImmutableList<MainTab>,
    currentTab: MainTab?,
    onClickTab: (MainTab) -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Blue100)
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color = White)
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                tabs.forEach { tab ->
                    MainTabItem(
                        tab = tab,
                        selected = tab == currentTab,
                        onClick = { onClickTab(tab) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.MainTabItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val icon =
        if (selected) painterResource(tab.selectedIconResId)
        else painterResource(tab.iconResId)
    val textColor = if (selected) Blue500 else Gray500

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(
                vertical = 4.dp
            )
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = icon,
                contentDescription = stringResource(tab.contentDescriptionResId),
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(tab.labelResId),
                color = textColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                style = QrizTheme.typography.label2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainBottomBarPreview1() {
    QrizTheme {
        MainBottomBar(
            isVisible = true,
            tabs = MainTab.entries.toImmutableList(),
            currentTab = MainTab.HOME,
            onClickTab = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainBottomBarPreview2() {
    QrizTheme {
        MainBottomBar(
            isVisible = true,
            tabs = MainTab.entries.toImmutableList(),
            currentTab = MainTab.CONCEPT_BOOK,
            onClickTab = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainBottomBarPreview3() {
    QrizTheme {
        MainBottomBar(
            isVisible = true,
            tabs = MainTab.entries.toImmutableList(),
            currentTab = MainTab.INCORRECT_ANSWERS_NOTE,
            onClickTab = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainBottomBarPreview4() {
    QrizTheme {
        MainBottomBar(
            isVisible = true,
            tabs = MainTab.entries.toImmutableList(),
            currentTab = MainTab.MY_PAGE,
            onClickTab = {},
        )
    }
}

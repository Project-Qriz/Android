package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.R
import com.qriz.app.core.designsystem.theme.QrizTheme

@Composable
fun QrizTopBar(
    title: String? = null,
    navigationType: NavigationType = NavigationType.NONE,
    background: Color = MaterialTheme.colorScheme.surface,
    onNavigationClick: () -> Unit,
    actions: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(background),
    ) {
        when (navigationType) {
            NavigationType.NONE -> Unit /* None */

            NavigationType.CLOSE -> {
                IconButton(
                    onClick = onNavigationClick,
                    modifier = Modifier.align(Alignment.CenterStart),
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                )
            }

            NavigationType.BACK -> {
                IconButton(
                    onClick = onNavigationClick,
                    modifier = Modifier.align(Alignment.CenterStart),
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                )
            }

            NavigationType.CANCEL -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                        .clickable { onNavigationClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = stringResource(R.string.cancel),
                        style = QrizTheme.typography.headline2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (title.isNullOrBlank().not()) {
            Text(
                text = title!!,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            actions?.invoke()
        }
    }
}

enum class NavigationType {
    NONE,
    CLOSE,
    BACK,
    CANCEL
}

@Preview(showBackground = true)
@Composable
fun QrizTopBarPreviewNone() {
    QrizTopBar(
        title = "Title",
        navigationType = NavigationType.NONE,
        onNavigationClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun QrizTopBarPreviewClose() {
    QrizTopBar(
        title = "Title",
        navigationType = NavigationType.CLOSE,
        onNavigationClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun QrizTopBarPreviewBack() {
    QrizTopBar(
        title = "Title",
        navigationType = NavigationType.BACK,
        onNavigationClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun QrizTopBarPreviewCancel() {
    QrizTopBar(
        title = "Title",
        navigationType = NavigationType.CANCEL,
        onNavigationClick = {}
    )
}

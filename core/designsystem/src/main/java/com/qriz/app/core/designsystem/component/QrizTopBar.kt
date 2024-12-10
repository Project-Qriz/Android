package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp

@Composable
fun QrizTopBar(
    title: String,
    navigationType: NavigationType = NavigationType.None,
    background: Color = MaterialTheme.colorScheme.surface,
    onNavigationClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .background(background),
    ) {
        when(navigationType) {
            NavigationType.None -> { /* None */ }
            NavigationType.Close -> {
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
            NavigationType.Back -> {
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
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )

    }
}

enum class NavigationType {
    None,
    Close,
    Back;
}

package com.qriz.app.core.designsystem.component.box

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

@LayoutScopeMarker
@Immutable
interface QrizBoxScope {
    fun Modifier.horizontalBias(ratio: Float): Modifier

    fun Modifier.verticalBias(ratio: Float): Modifier
}

internal object QrizBoxScopeInstance : QrizBoxScope {
    override fun Modifier.horizontalBias(ratio: Float): Modifier = layout(
        measure = { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val parentWidth = constraints.maxWidth
            val xOffset = maxOf(
                0,
                (parentWidth * ratio - placeable.width).toInt()
            )

            layout(
                placeable.width,
                placeable.height
            ) {
                placeable.placeRelative(
                    xOffset,
                    0
                )
            }
        },
    )

    override fun Modifier.verticalBias(ratio: Float): Modifier = layout(
        measure = { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val parentHeight = constraints.maxHeight
            val yOffset = maxOf(
                0,
                (parentHeight * ratio - placeable.height).toInt()
            )

            layout(
                placeable.width,
                placeable.height
            ) {
                placeable.placeRelative(
                    0,
                    yOffset
                )
            }
        },
    )
}


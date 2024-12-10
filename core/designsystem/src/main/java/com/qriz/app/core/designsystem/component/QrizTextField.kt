package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray700

@Composable
fun QrizTextFiled(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = Int.MAX_VALUE,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = Gray700),
    enabled: Boolean = true,
    singleLine: Boolean = false,
    hint: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    supportingText: SupportingText? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    trailing: (@Composable () -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length > maxLength) return@BasicTextField
            else onValueChange(it)
        },
        enabled = enabled,
        modifier = modifier,
        singleLine = singleLine,
        textStyle = style.copy(
            platformStyle = PlatformTextStyle(
                includeFontPadding = true
            ),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        ),
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(10.dp),
                            )
                            .padding(contentPadding),
                    ) {
                        Box(modifier = Modifier.height(style.lineHeight.value.toInt().dp)) {
                            innerTextField()
                        }

                        if (hint != null && value.isEmpty()) {
                            Text(
                                text = hint,
                                style = style.copy(color = Gray300)
                            )
                        }
                    }

                    val trailingPadding = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
                    if (trailing != null) Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = trailingPadding)
                            .height(24.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        trailing()
                    }
                }
                if (supportingText != null) {
                    Text(
                        text = supportingText.message,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = supportingText.color,
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
    )
}

@Immutable
data class SupportingText(
    val message: String,
    val color: Color,
)

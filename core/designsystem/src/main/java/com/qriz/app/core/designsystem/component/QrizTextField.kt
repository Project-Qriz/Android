package com.qriz.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.theme.Gray700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrizTextFiled(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
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
        onValueChange = onValueChange,
        modifier = modifier,
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
            TextFieldDefaults.DecorationBox(
                value = value,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = remember { MutableInteractionSource() },
                innerTextField = innerTextField,
                contentPadding = contentPadding,
                placeholder = {
                    if (hint != null) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = true
                                ),
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                )
                            ),
                        )
                    }
                },
                supportingText = {
                    if (supportingText != null) {
                        Text(
                            text = supportingText.message,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(supportingText.color),
                            ),
                        )
                    }
                },
                container = {
                    Box(
                        modifier = Modifier.background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp),
                        ),
                    )
                },
                trailingIcon = trailing,
            )
        },
    )
}

data class SupportingText(
    val message: String,
    val color: Int,
)

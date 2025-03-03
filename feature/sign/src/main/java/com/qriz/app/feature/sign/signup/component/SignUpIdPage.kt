package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState
import com.qriz.app.feature.sign.signup.SignUpUiState.UserIdValidationState.AVAILABLE
import com.qriz.app.feature.sign.signup.SignUpUiState.UserIdValidationState.NOT_AVAILABLE
import com.quiz.app.core.data.user.user_api.model.ID_MAX_LENGTH

@Composable
fun SignUpIdPage(
    id: String,
    validationState: SignUpUiState.UserIdValidationState,
    errorMessage: String,
    focusState: SignUpUiState.FocusState,
    onChangeUserId: (String) -> Unit,
    onClickIdDuplicateCheck: () -> Unit,
    onClickNextPage: () -> Unit,
    onFocused: () -> Unit,
) {
    val supportingText = when {
        validationState == AVAILABLE -> SupportingText(
            message = stringResource(R.string.id_can_be_used),
            color = Mint800,
        )

        errorMessage.isNotBlank() -> SupportingText(
            message = errorMessage,
            color = Red700
        )

        else -> SupportingText(
            message = "${id.length}/$ID_MAX_LENGTH",
            color = Black,
            isBorderColorRequired = false
        )
    }

    val borderColor = when {
        validationState == AVAILABLE -> Mint800
        validationState == NOT_AVAILABLE -> Red700
        focusState == SignUpUiState.FocusState.ID -> Gray800
        else -> Gray200
    }

    SignUpBasePage(
        title = stringResource(R.string.sign_up_id_page_title),
        buttonText = stringResource(R.string.sign_up_id_page_button_text),
        buttonEnabled = validationState == AVAILABLE,
        onButtonClick = onClickNextPage,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            QrizTextFiled(
                value = id,
                onValueChange = onChangeUserId,
                supportingText = supportingText,
                singleLine = true,
                hint = stringResource(R.string.sign_up_id_page_hint),
                maxLength = ID_MAX_LENGTH,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                ),
                containerColor = White,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { if (it.isFocused) onFocused() },
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                ),
                trailing = {
                    if (id.isNotEmpty()) {
                        IconButton(
                            onClick = { onChangeUserId("") },
                            modifier = Modifier.size(20.dp),
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.delete_icon),
                                contentDescription = null,
                                tint = Gray300,
                            )
                        }
                    }
                }
            )
            OutlinedButton(
                onClick = onClickIdDuplicateCheck,
                border = BorderStroke(
                    width = 1.dp,
                    color = Gray200,
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    stringResource(R.string.sign_up_id_page_check_duplicate),
                    color = if (id.isNotBlank()) Gray800 else Gray200,
                    style = QrizTheme.typography.subhead,
                )
            }
        }
    }
}

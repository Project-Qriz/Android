package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.FocusState
import com.quiz.app.core.data.user.user_api.model.USER_NAME_MAX_LENGTH

@Composable
fun SignUpNamePage(
    name: String,
    isValidName: Boolean,
    errorMessage: String,
    focusState: FocusState,
    onChangeUserName: (String) -> Unit,
    onClickNextPage: () -> Unit,
    onFocused: () -> Unit,
) {
    val supportingText: SupportingText? = if (errorMessage.isNotBlank()) SupportingText(
        message = errorMessage,
        color = Red700
    ) else null

    val nameBorderColor = if (focusState == FocusState.NAME) Gray800 else Gray200

    SignUpBasePage(
        title = stringResource(R.string.sign_up_name_page_title),
        buttonEnabled = isValidName,
        buttonText = stringResource(R.string.sign_up_name_page_button_text),
        onButtonClick = onClickNextPage,
    ) {
        QrizTextFiled(
            value = name,
            supportingText = supportingText,
            onValueChange = onChangeUserName,
            singleLine = true,
            maxLength = USER_NAME_MAX_LENGTH,
            hint = stringResource(R.string.sign_up_name_page_hint),
            borderStroke = BorderStroke(
                width = 1.dp,
                color = nameBorderColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .onFocusChanged {
                    if (it.isFocused) {
                        onFocused()
                    }
                },
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            ),
            containerColor = White,
            trailing = {
                if (name.isNotEmpty()) {
                    IconButton(
                        onClick = { onChangeUserName("") },
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
    }
}

@Preview
@Composable
fun SignUpNamePagePreview() {
    QrizTheme {
        SignUpNamePage(
            name = "홍길동",
            focusState = FocusState.NAME,
            isValidName = true,
            errorMessage = "",
            onChangeUserName = {},
            onFocused = {},
            onClickNextPage = {}
        )
    }
}

package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiState.Companion.USER_NAME_MAX_LENGTH

@Composable
fun SignUpNamePage(
    name: String,
    isValidName: Boolean,
    errorMessage: String,
    onChangeUserName: (String) -> Unit,
    onClickNextPage: () -> Unit,
) {
    val supportingText: SupportingText? = if (errorMessage.isNotBlank()) SupportingText(
        message = errorMessage,
        color = MaterialTheme.colorScheme.error
    ) else null

    SignUpBasePage(
        title = stringResource(R.string.sign_up_name_page_title),
        subTitle = stringResource(R.string.sign_up_name_page_sub_title),
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
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp,
            )
        )
    }
}

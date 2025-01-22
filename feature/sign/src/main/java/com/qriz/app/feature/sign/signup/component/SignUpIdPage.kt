package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.component.SupportingText
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue600
import com.qriz.app.core.designsystem.theme.Mint800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red800
import com.qriz.app.feature.sign.R
import com.quiz.app.core.data.user.user_api.model.ID_MAX_LENGTH

@Composable
fun SignUpIdPage(
    id: String,
    isAvailableId: Boolean,
    errorMessage: String,
    onChangeUserId: (String) -> Unit,
    onClickIdDuplicateCheck: () -> Unit,
    onClickNextPage: () -> Unit,
) {
    val supportingText = when {
        isAvailableId -> SupportingText(
            message = stringResource(R.string.id_can_be_used),
            color = Mint800
        )

        errorMessage.isNotBlank() -> SupportingText(
            message = errorMessage,
            color = Red800
        )

        else -> SupportingText(
            message = "${id.length}/$ID_MAX_LENGTH",
            color = Black,
            isBorderColorRequired = false
        )
    }

    SignUpBasePage(
        title = stringResource(R.string.sign_up_id_page_title),
        subTitle = stringResource(R.string.sign_up_id_page_sub_title),
        buttonText = stringResource(R.string.sign_up_id_page_button_text),
        buttonEnabled = isAvailableId,
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
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 14.dp,
                )
            )
            OutlinedButton(
                onClick = onClickIdDuplicateCheck,
                border = BorderStroke(
                    width = 1.dp,
                    color = Blue600
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
                    color = Blue600,
                    style = QrizTheme.typography.subhead,
                )
            }
        }
    }
}

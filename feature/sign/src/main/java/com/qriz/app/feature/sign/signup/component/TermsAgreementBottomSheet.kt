package com.qriz.app.feature.sign.signup.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.sign.R
import com.qriz.app.core.designsystem.R as DSR

data class TermsAgreementState(
    val agreeToTermsOfService: Boolean = false,
    val agreeToPrivacyPolicy: Boolean = false,
) {
    val isAllAgreed: Boolean
        get() = agreeToTermsOfService && agreeToPrivacyPolicy

    val canProceed: Boolean
        get() = agreeToTermsOfService && agreeToPrivacyPolicy
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAgreementBottomSheet(
    termsState: TermsAgreementState,
    onDismiss: () -> Unit,
    onAgreeAll: () -> Unit,
    onToggleTermsOfService: () -> Unit,
    onTogglePrivacyPolicy: () -> Unit,
    onViewTermsOfService: () -> Unit,
    onViewPrivacyPolicy: () -> Unit,
    onConfirm: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        dragHandle = null,
    ) {
        TermsAgreementBottomSheetContent(
            termsState = termsState,
            onDismiss = onDismiss,
            onAgreeAll = onAgreeAll,
            onToggleTermsOfService = onToggleTermsOfService,
            onTogglePrivacyPolicy = onTogglePrivacyPolicy,
            onViewTermsOfService = onViewTermsOfService,
            onViewPrivacyPolicy = onViewPrivacyPolicy,
            onConfirm = onConfirm,
        )
    }
}

@Composable
private fun TermsAgreementBottomSheetContent(
    termsState: TermsAgreementState,
    onDismiss: () -> Unit,
    onAgreeAll: () -> Unit,
    onToggleTermsOfService: () -> Unit,
    onTogglePrivacyPolicy: () -> Unit,
    onViewTermsOfService: () -> Unit,
    onViewPrivacyPolicy: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "약관동의",
                style = QrizTheme.typography.headline1,
                color = Gray800
            )

            Icon(
                painter = painterResource(DSR.drawable.close_icon),
                contentDescription = "닫기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                tint = Gray700
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // All Agreement Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(
                    width = 1.dp,
                    color = Gray100,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    onClick = onAgreeAll,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    if (termsState.isAllAgreed) DSR.drawable.ic_check_box_on
                    else DSR.drawable.ic_check_box_off
                ),
                contentDescription = "전체 동의",
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Text(
                text = "전체 동의",
                style = QrizTheme.typography.headline2,
                color = Gray800
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Individual Terms
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TermsItem(
                text = "서비스 이용약관 동의",
                isRequired = true,
                isChecked = termsState.agreeToTermsOfService,
                onToggle = onToggleTermsOfService,
                onViewDetails = onViewTermsOfService
            )

            TermsItem(
                text = "개인정보 처리방침 동의",
                isRequired = true,
                isChecked = termsState.agreeToPrivacyPolicy,
                onToggle = onTogglePrivacyPolicy,
                onViewDetails = onViewPrivacyPolicy
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Confirm Button
        QrizButton(
            text = "가입하기",
            onClick = onConfirm,
            enable = termsState.canProceed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
private fun TermsItem(
    text: String,
    isRequired: Boolean,
    isChecked: Boolean,
    onToggle: () -> Unit,
    onViewDetails: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onToggle,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.check_icon),
            contentDescription = if (isChecked) "동의됨" else "동의 안 됨",
            modifier = Modifier.size(24.dp),
            tint = if (isChecked) Blue500 else Blue100
        )

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        Text(
            text = buildAnnotatedString {
                append(text)
                if (isRequired) {
                    withStyle(style = SpanStyle(color = Red700)) {
                        append("(필수)")
                    }
                }
            },
            style = QrizTheme.typography.body1,
            color = Gray700,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(DSR.drawable.ic_keyboard_arrow_right),
            contentDescription = "약관 보기",
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = onViewDetails,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            tint = Gray500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsAgreementBottomSheetPreview() {
    QrizTheme {
        TermsAgreementBottomSheetContent(
            termsState = TermsAgreementState(
                agreeToTermsOfService = true,
                agreeToPrivacyPolicy = true
            ),
            onDismiss = {},
            onAgreeAll = {},
            onToggleTermsOfService = {},
            onTogglePrivacyPolicy = {},
            onViewTermsOfService = {},
            onViewPrivacyPolicy = {},
            onConfirm = {}
        )
    }
}

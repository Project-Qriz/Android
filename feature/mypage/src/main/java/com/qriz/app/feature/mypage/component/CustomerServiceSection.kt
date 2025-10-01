package com.qriz.app.feature.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.R as DSR
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.mypage.BuildConfig
import com.qriz.app.feature.mypage.R

@Composable
internal fun CustomerServiceSection(
    modifier: Modifier = Modifier,
    onClickServiceTerms: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickVersion: () -> Unit,
) {
    QrizCard(
        modifier = modifier,
        cornerRadius = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.my_customer_service),
                style = QrizTheme.typography.headline1.copy(color = Gray800)
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Blue100,
            )
            CustomerServiceItem(
                title = stringResource(R.string.my_service_terms),
                onClick = onClickServiceTerms,
            )
            CustomerServiceItem(
                title = stringResource(R.string.my_privacy_terms),
                onClick = onClickPrivacyPolicy,
            )
            CustomerServiceItem(
                title = stringResource(R.string.my_app_version),
                subTitle = stringResource(
                    R.string.my_app_version_format,
                    "1",
                    "1.0.0",
                ),
                onClick = onClickVersion,
            )
        }
    }
}

@Preview
@Composable
private fun CustomerServiceSectionPreview() {
    QrizTheme {
        CustomerServiceSection(
            onClickServiceTerms = {},
            onClickPrivacyPolicy = {},
            onClickVersion = {},
        )
    }
}

@Composable
private fun CustomerServiceItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = QrizTheme.typography.subhead.copy(
                fontWeight = FontWeight.Bold,
                color = Gray800,
            )
        )

        if (subTitle != null) {
            Text(
                text = subTitle,
                style = QrizTheme.typography.body1.copy(color = Gray400),
            )
        }

        Icon(
            imageVector = ImageVector.vectorResource(DSR.drawable.ic_keyboard_arrow_right),
            tint = Gray400,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomerServiceItemPreview() {
    QrizTheme {
        CustomerServiceItem(
            title = "고객센터",
            subTitle = "02-1234-5678",
            onClick = {},
        )
    }
}

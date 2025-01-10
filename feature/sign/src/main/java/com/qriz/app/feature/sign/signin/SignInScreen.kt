package com.qriz.app.feature.sign.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizTextFiled
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.R as DSR

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onClickSignUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignInContent(
        id = state.id,
        password = state.password,
        showPassword = state.showPassword,
        onIdChange = viewModel::updateId,
        onPasswordChange = viewModel::updatePassword,
        onLogin = viewModel::login,
        onChangeShowPassword = viewModel::updateShowPassword,
        onClickSignUp = onClickSignUp,
    )
}

@Composable
fun SignInContent(
    id: String,
    password: String,
    showPassword: Boolean,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onChangeShowPassword: (Boolean) -> Unit,
    onClickSignUp: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(DSR.drawable.img_logo),
            contentDescription = "",
            modifier = Modifier
                .padding(
                    top = 48.dp,
                    bottom = 32.dp
                )
                .height(124.dp)

        )

        QrizTextFiled(
            value = id,
            onValueChange = onIdChange,
            hint = "아이디를 입력해주세요.",
            contentPadding = PaddingValues(
                vertical = 19.dp,
                horizontal = 16.dp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        )

        QrizTextFiled(
            value = password,
            onValueChange = onPasswordChange,
            hint = "비밀번호를 입력해주세요.",
            contentPadding = PaddingValues(
                vertical = 19.dp,
                horizontal = 16.dp,
            ),
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailing = {
                if (password.isNotEmpty()) IconButton(onClick = { onChangeShowPassword(!showPassword) }) {
                    Icon(
                        imageVector = if (showPassword) ImageVector.vectorResource(DSR.drawable.ic_visible_password)
                        else ImageVector.vectorResource(DSR.drawable.ic_invisible_password),
                        contentDescription = ""
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 8.dp),
        )

        QrizButton(
            text = "로그인",
            enable = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 12.dp),
            onClick = onLogin,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color(0xFFA8AFB6),
            )
            Text(
                text = "다른 방법으로 로그인하기",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFA8AFB6)),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color(0xFFA8AFB6),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            val color = Color(0xFFA8AFB6)
            val textStyle = MaterialTheme.typography.bodySmall.copy(color = color)

            Text(
                "회원가입",
                style = textStyle,
                modifier = Modifier
                    .clickable(onClick = onClickSignUp)
                    .padding(5.dp)
            )
            VerticalDivider(
                thickness = 1.dp,
                color = color,
                modifier = Modifier.height(13.dp)
            )
            Text(
                "아이디 찾기",
                style = textStyle,
                modifier = Modifier.padding(5.dp)
            )
            VerticalDivider(
                thickness = 1.dp,
                color = color,
                modifier = Modifier.height(13.dp)
            )
            Text(
                "비밀번호 찾기",
                style = textStyle,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun SignInScreenPreview() {
    QrizTheme {
        SignInContent(
            id = "",
            password = "",
            showPassword = false,
            onLogin = {},
            onClickSignUp = {},
            onIdChange = {},
            onPasswordChange = {},
            onChangeShowPassword = {},
        )
    }
}

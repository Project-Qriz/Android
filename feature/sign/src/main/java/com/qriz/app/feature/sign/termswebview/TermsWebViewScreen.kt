package com.qriz.app.feature.sign.termswebview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizTopBar

@Composable
fun TermsWebViewScreen(
    url: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        QrizTopBar(
            title = getTitle(url),
            navigationType = NavigationType.BACK,
            onNavigationClick = onBack
        )

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = false
                        displayZoomControls = false
                    }
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

private fun getTitle(url: String): String {
    return when {
        url.contains("privacy") -> "개인정보 처리방침"
        url.contains("term") -> "서비스 이용약관"
        else -> "약관"
    }
}
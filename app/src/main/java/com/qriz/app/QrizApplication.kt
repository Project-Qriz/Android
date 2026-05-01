package com.qriz.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.kakao.sdk.common.KakaoSdk
import com.qriz.app.core.ui.common.resource.AdConfig
import com.qriz.app.core.ui.common.util.PdfPageDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QrizApplication: Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()
        AdConfig.GOOGLE_ADS_SDK_ID = BuildConfig.GOOGLE_ADS_SDK_ID
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY_PROPERTY)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        // Coil Custom Decoder
        return ImageLoader.Builder(this)
            .components { add(PdfPageDecoder.Factory()) }
            .build()
    }
}
